// PATTERN: MVC — Controller
// Handles incident queue interactions from SOC analysts

const IncidentQueueModel = require('../models/IncidentQueueModel');

class IncidentController {

    constructor() {
        this.model = new IncidentQueueModel();
    }

    // GET /api/incidents
    getIncidents(req, res) {
        try {
            const incidents = this.model.getIncidents();
            res.json({ status: 'ok', count: incidents.length, incidents });
        } catch (err) {
            res.status(500).json({ error: err.message });
        }
    }

    // POST /api/incidents/:id/annotate
    annotateIncident(req, res) {
        try {
            const { annotation } = req.body;
            const result = this.model.addAnnotation(req.params.id, annotation);
            res.json({ status: 'annotated', incidentId: req.params.id, result });
        } catch (err) {
            res.status(400).json({ error: err.message });
        }
    }

    // POST /api/incidents/:id/override
    overrideDecision(req, res) {
        try {
            const { decision, reason } = req.body;
            const result = this.model.overrideDecision(req.params.id, decision, reason);
            res.json({ status: 'overridden', result });
        } catch (err) {
            res.status(400).json({ error: err.message });
        }
    }
}

module.exports = IncidentController;
