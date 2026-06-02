// PATTERN: MVC — Model
// Manages incident queue data and analyst actions

class IncidentQueueModel {

    constructor() {
        this.incidents = [];
        this.annotations = {};
    }

    getIncidents() {
        return [...this.incidents];
    }

    addAnnotation(incidentId, annotation) {
        if (!this.annotations[incidentId]) {
            this.annotations[incidentId] = [];
        }
        this.annotations[incidentId].push({
            text: annotation,
            timestamp: new Date().toISOString()
        });
        return this.annotations[incidentId];
    }

    overrideDecision(incidentId, decision, reason) {
        const incident = this.incidents.find(i => i.id === incidentId);
        if (!incident) throw new Error('Incident not found: ' + incidentId);
        incident.analystOverride = { decision, reason, at: new Date().toISOString() };
        return incident;
    }

    // Called by Observer when IncidentCreated event arrives
    onIncidentCreated(payload) {
        this.incidents.unshift({
            id: payload.incidentId || 'unknown',
            severity: payload.severity || 'LOW',
            state: 'NEW',
            createdAt: new Date().toISOString()
        });
    }
}

module.exports = IncidentQueueModel;
