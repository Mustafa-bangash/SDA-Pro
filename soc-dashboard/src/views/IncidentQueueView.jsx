import React, { useState } from 'react';

// MVC: View — incident queue with state machine visualization
// PATTERN: Observer — updates when IncidentStateChanged events arrive
// PATTERN: State — shows current state and valid transitions
// SERVICE: incident-management-service:8083
export default function IncidentQueueView() {
  const [incidents, setIncidents] = useState([
    { id: 'INC-001', title: 'APT29 Lateral Movement Campaign', severity: 'CRITICAL', state: 'CONTAINMENT', analyst: 'Student A', created: '10:15', alerts: 6, strategy: 'AGGRESSIVE' },
    { id: 'INC-002', title: 'CrowdStrike Malware on DB Server', severity: 'HIGH', state: 'UNDER_TRIAGE', analyst: 'Student B', created: '10:28', alerts: 2, strategy: 'BALANCED' },
    { id: 'INC-003', title: 'Firewall DDoS Pattern Detected', severity: 'CRITICAL', state: 'NEW', analyst: 'Unassigned', created: '10:37', alerts: 3, strategy: 'AGGRESSIVE' },
    { id: 'INC-004', title: 'Privilege Escalation — CloudSIEM', severity: 'HIGH', state: 'ERADICATION', analyst: 'Student C', created: '09:55', alerts: 4, strategy: 'BALANCED' },
    { id: 'INC-005', title: 'Repeated Failed Logins — DC01', severity: 'MEDIUM', state: 'RECOVERY', analyst: 'Student A', created: '09:30', alerts: 1, strategy: 'CONSERVATIVE' },
  ]);

  const stateColors = { NEW: '#4a5568', UNDER_TRIAGE: '#9f7aea', CONTAINMENT: '#f6ad55', ERADICATION: '#fc8181', RECOVERY: '#48bb78', POST_INCIDENT_REVIEW: '#63b3ed', CLOSED: '#2d3748' };
  const severityColors = { CRITICAL: '#fc8181', HIGH: '#f6ad55', MEDIUM: '#63b3ed', LOW: '#48bb78' };
  const stateOrder = ['NEW', 'UNDER_TRIAGE', 'CONTAINMENT', 'ERADICATION', 'RECOVERY', 'POST_INCIDENT_REVIEW', 'CLOSED'];

  const advance = (id) => {
    setIncidents(prev => prev.map(inc => {
      if (inc.id !== id) return inc;
      const idx = stateOrder.indexOf(inc.state);
      if (idx < stateOrder.length - 1) return { ...inc, state: stateOrder[idx + 1] };
      return inc;
    }));
  };

  const styles = {
    title: { fontSize: '16px', fontWeight: '600', color: '#e2e8f0', marginBottom: '1.5rem' },
    card: { background: '#1a1d2e', border: '1px solid #2d3748', borderRadius: '8px', padding: '1.2rem 1.5rem', marginBottom: '10px' },
    row: { display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '8px' },
    incId: { fontSize: '12px', color: '#4a5568', fontWeight: '600' },
    incTitle: { fontSize: '14px', color: '#e2e8f0', fontWeight: '500', flex: 1 },
    badge: (color) => ({ background: color + '22', color, padding: '2px 8px', borderRadius: '3px', fontSize: '11px', fontWeight: '600' }),
    meta: { fontSize: '11px', color: '#4a5568', display: 'flex', gap: '16px', marginTop: '6px' },
    btn: { padding: '5px 12px', borderRadius: '4px', border: '1px solid #4a5568', background: 'transparent', color: '#a0aec0', cursor: 'pointer', fontSize: '12px' },
    stateMachine: { display: 'flex', alignItems: 'center', gap: '4px', marginTop: '8px', flexWrap: 'wrap' },
    stateStep: (active, color) => ({ padding: '2px 8px', borderRadius: '3px', fontSize: '10px', fontWeight: active ? '700' : '400', background: active ? color + '33' : 'transparent', color: active ? color : '#4a5568', border: active ? `1px solid ${color}` : '1px solid transparent' }),
  };

  return (
    <div>
      <div style={styles.title}>Incident Queue — State Machine (7 states)</div>
      {incidents.map(inc => (
        <div key={inc.id} style={styles.card}>
          <div style={styles.row}>
            <span style={styles.incId}>{inc.id}</span>
            <span style={styles.incTitle}>{inc.title}</span>
            <span style={styles.badge(severityColors[inc.severity])}>{inc.severity}</span>
            <span style={styles.badge(stateColors[inc.state])}>{inc.state.replace('_',' ')}</span>
          </div>
          <div style={styles.stateMachine}>
            {stateOrder.slice(0,-1).map((s,i) => (
              <React.Fragment key={s}>
                <span style={styles.stateStep(inc.state===s, stateColors[s])}>{s.replace('_',' ')}</span>
                {i < stateOrder.length-2 && <span style={{color:'#4a5568',fontSize:'10px'}}>→</span>}
              </React.Fragment>
            ))}
          </div>
          <div style={styles.meta}>
            <span>👤 {inc.analyst}</span>
            <span>🕐 {inc.created}</span>
            <span>🚨 {inc.alerts} alerts</span>
            <span>⚡ Strategy: {inc.strategy}</span>
            {inc.state !== 'CLOSED' && <button style={styles.btn} onClick={() => advance(inc.id)}>Advance State →</button>}
          </div>
        </div>
      ))}
    </div>
  );
}
