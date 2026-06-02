// PATTERN: MVC — View
// Renders incident queue for SOC analyst display

class IncidentQueueView {

    render(incidents) {
        return {
            component: 'IncidentQueue',
            pattern: 'MVC-View',
            incidentCount: incidents.length,
            incidents: incidents.map(i => this.renderIncident(i))
        };
    }

    renderIncident(incident) {
        return {
            id: incident.id,
            severity: incident.severity,
            state: incident.state,
            createdAt: incident.createdAt,
            hasOverride: !!incident.analystOverride
        };
    }
}

module.exports = IncidentQueueView;
