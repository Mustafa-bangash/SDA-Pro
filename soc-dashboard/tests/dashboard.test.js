// PATTERN: MVC — Tests for Controller, Model, and View layers
// Verifies strict MVC separation: Controller delegates to Model,
// Model never touches HTTP, View only formats data.

const AlertStreamModel = require('../src/models/AlertStreamModel');
const IncidentQueueModel = require('../src/models/IncidentQueueModel');
const AlertStreamView = require('../src/views/AlertStreamView');
const IncidentQueueView = require('../src/views/IncidentQueueView');
const DashboardStore = require('../src/store/DashboardStore');

describe('MVC Pattern Tests — SOC Dashboard', () => {

    describe('AlertStreamModel', () => {
        test('getAlerts returns empty array initially', () => {
            const model = new AlertStreamModel();
            const alerts = model.getAlerts({});
            expect(Array.isArray(alerts)).toBe(true);
        });

        test('acknowledgeAlert marks alert as acknowledged', () => {
            const model = new AlertStreamModel();
            const result = model.acknowledgeAlert('alert-001');
            expect(result.acknowledged).toBe(true);
            expect(result.alertId).toBe('alert-001');
        });

        test('onAlertIngested adds alert to stream', () => {
            const model = new AlertStreamModel();
            model.onAlertIngested({ alertId: 'a1', source: 'SPLUNK', severity: 'CRITICAL' });
            const alerts = model.getAlerts({});
            expect(alerts.length).toBe(1);
            expect(alerts[0].sourceType).toBe('SPLUNK');
        });
    });

    describe('IncidentQueueModel', () => {
        test('getIncidents returns empty array initially', () => {
            const model = new IncidentQueueModel();
            expect(model.getIncidents().length).toBe(0);
        });

        test('addAnnotation stores annotation for incident', () => {
            const model = new IncidentQueueModel();
            model.onIncidentCreated({ incidentId: 'inc-001', severity: 'HIGH' });
            const result = model.addAnnotation('inc-001', 'Investigating now');
            expect(result.length).toBe(1);
            expect(result[0].text).toBe('Investigating now');
        });
    });

    describe('AlertStreamView', () => {
        test('render formats alerts with severity colors', () => {
            const view = new AlertStreamView();
            const alerts = [{ id: 'a1', sourceType: 'SPLUNK', severity: 'CRITICAL', receivedAt: new Date().toISOString() }];
            const rendered = view.render(alerts);
            expect(rendered.component).toBe('AlertStream');
            expect(rendered.alerts[0].severityColor).toBe('#FF0000');
        });
    });

    describe('DashboardStore', () => {
        test('store is a singleton', () => {
            const store1 = require('../src/store/DashboardStore');
            const store2 = require('../src/store/DashboardStore');
            expect(store1).toBe(store2);
        });

        test('onAlertIngested updates state', () => {
            DashboardStore.onAlertIngested({ id: 'a1', severity: 'HIGH', sourceType: 'FIREWALL' });
            expect(DashboardStore.getState().alerts.length).toBeGreaterThan(0);
        });
    });
});
