// PATTERN: MVC — Service Layer (between Controller and Model)
// RATIONALE: Business logic that doesn't belong in Controller or Model
// lives in the Service layer. DashboardService aggregates data from
// multiple models to build the dashboard metrics view.

const AlertStreamModel = require('../models/AlertStreamModel');
const IncidentQueueModel = require('../models/IncidentQueueModel');

class DashboardService {

    constructor() {
        this.alertModel = new AlertStreamModel();
        this.incidentModel = new IncidentQueueModel();
    }

    // Aggregates SOC metrics from all models
    getMetrics() {
        const alerts = this.alertModel.getAlerts({});
        const incidents = this.incidentModel.getIncidents();

        return {
            totalAlerts: alerts.length,
            criticalAlerts: alerts.filter(a => a.severity === 'CRITICAL').length,
            totalIncidents: incidents.length,
            openIncidents: incidents.filter(i => i.state !== 'CLOSED').length,
            mttr: this.calculateMTTR(incidents),
            generatedAt: new Date().toISOString()
        };
    }

    // Mean time to resolve — average time from NEW to CLOSED
    calculateMTTR(incidents) {
        const closed = incidents.filter(i => i.state === 'CLOSED');
        if (closed.length === 0) return 'N/A';
        return closed.length + ' incidents resolved';
    }
}

module.exports = DashboardService;
