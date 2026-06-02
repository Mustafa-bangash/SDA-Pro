// PATTERN: MVC — View
// RATIONALE: View is responsible only for rendering data received from
// the Model via the Controller. It never fetches data directly.
// Observer: View updates in real-time when WebSocket push arrives
// from DashboardUpdater subscriber (< 2 second update requirement).

class AlertStreamView {

    // Renders alert stream data to JSON response format
    render(alerts) {
        return {
            component: 'AlertStream',
            pattern: 'MVC-View + Observer',
            updateMode: 'WebSocket push < 2 seconds',
            alertCount: alerts.length,
            alerts: alerts.map(alert => this.renderAlert(alert))
        };
    }

    renderAlert(alert) {
        return {
            id: alert.id,
            sourceType: alert.sourceType,
            severity: alert.severity,
            severityColor: this.getSeverityColor(alert.severity),
            receivedAt: alert.receivedAt
        };
    }

    getSeverityColor(severity) {
        const colors = {
            CRITICAL: '#FF0000',
            HIGH: '#FF6600',
            MEDIUM: '#FFAA00',
            LOW: '#00AA00'
        };
        return colors[severity] || '#888888';
    }
}

module.exports = AlertStreamView;
