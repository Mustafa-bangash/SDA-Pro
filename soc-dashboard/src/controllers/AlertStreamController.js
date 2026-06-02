// PATTERN: MVC — Controller
// RATIONALE: Controller handles incoming HTTP/WebSocket requests and
// delegates to the Model. It never contains business logic or UI code.
// This strict separation is the core of the MVC architecture style.

const AlertStreamModel = require('../models/AlertStreamModel');

class AlertStreamController {

    constructor() {
        this.model = new AlertStreamModel();
    }

    // GET /api/alerts — returns all alerts for the stream view
    getAlerts(req, res) {
        try {
            const filters = {
                severity: req.query.severity || null,
                source: req.query.source || null,
                limit: parseInt(req.query.limit) || 50
            };
            const alerts = this.model.getAlerts(filters);
            res.json({ status: 'ok', count: alerts.length, alerts });
        } catch (err) {
            res.status(500).json({ error: err.message });
        }
    }

    // POST /api/alerts/:id/acknowledge
    acknowledgeAlert(req, res) {
        try {
            const result = this.model.acknowledgeAlert(req.params.id);
            res.json({ status: 'acknowledged', alertId: req.params.id, result });
        } catch (err) {
            res.status(400).json({ error: err.message });
        }
    }

    // GET /api/health
    health(req, res) {
        res.json({ status: 'soc-dashboard is running', pattern: 'MVC' });
    }
}

module.exports = AlertStreamController;
