// PATTERN: MVC — Model
// RATIONALE: Model owns all data access and business logic.
// Controllers and Views never touch raw data directly.
// Observer: Model receives WebSocket push updates from EventBusPublisher
// and maintains the alert stream state.

class AlertStreamModel {

    constructor() {
        // In-memory store (production: REST call to alert-ingestion-service)
        this.alerts = [];
        this.acknowledgedIds = new Set();
    }

    getAlerts(filters = {}) {
        let result = [...this.alerts];
        if (filters.severity) {
            result = result.filter(a => a.severity === filters.severity);
        }
        if (filters.source) {
            result = result.filter(a => a.sourceType === filters.source);
        }
        return result.slice(0, filters.limit || 50);
    }

    acknowledgeAlert(alertId) {
        this.acknowledgedIds.add(alertId);
        return { acknowledged: true, alertId };
    }

    // Called by Observer when AlertIngested event arrives
    onAlertIngested(payload) {
        this.alerts.unshift({
            id: payload.alertId || 'unknown',
            sourceType: payload.source || 'UNKNOWN',
            severity: payload.severity || 'LOW',
            receivedAt: new Date().toISOString()
        });
        // Keep only last 500 alerts in memory
        if (this.alerts.length > 500) this.alerts.pop();
    }
}

module.exports = AlertStreamModel;
