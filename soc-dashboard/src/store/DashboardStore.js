// PATTERN: MVC — Store (State Management)
// Central state store for the SOC dashboard.
// Observer: Store is updated by WebSocket push events from DashboardUpdater.
// All View components read from the store rather than fetching independently.

class DashboardStore {

    constructor() {
        this.state = {
            alerts: [],
            incidents: [],
            metrics: {},
            connectionStatus: 'DISCONNECTED',
            lastUpdated: null
        };
        this.listeners = [];
    }

    // Subscribe to state changes
    subscribe(listener) {
        this.listeners.push(listener);
    }

    // Update state and notify all listeners (Observer within MVC)
    setState(newState) {
        this.state = { ...this.state, ...newState, lastUpdated: new Date().toISOString() };
        this.listeners.forEach(listener => listener(this.state));
    }

    getState() {
        return this.state;
    }

    // Called by DashboardUpdater Observer when WebSocket push arrives
    onAlertIngested(alertData) {
        const alerts = [alertData, ...this.state.alerts].slice(0, 500);
        this.setState({ alerts });
    }

    onIncidentCreated(incidentData) {
        const incidents = [incidentData, ...this.state.incidents];
        this.setState({ incidents });
    }

    onIncidentStateChanged(update) {
        const incidents = this.state.incidents.map(i =>
            i.id === update.incidentId ? { ...i, state: update.newState } : i
        );
        this.setState({ incidents });
    }
}

module.exports = new DashboardStore(); // Singleton store instance
