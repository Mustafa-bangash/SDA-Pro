import React, { useState } from 'react';

// MVC: View — response action console
// PATTERN: Strategy — analyst selects response strategy
// PATTERN: Facade — all actions go through IncidentResponseFacade
// PATTERN: Decorator — actions decorated with audit + approval + rollback
// SERVICE: response-orchestration-service:8084
export default function ResponseConsoleView() {
  const [selectedStrategy, setSelectedStrategy] = useState('BALANCED');
  const [log, setLog] = useState([]);
  const [executing, setExecuting] = useState(false);

  const strategies = [
    { id: 'AGGRESSIVE', label: 'Aggressive Containment', color: '#fc8181', desc: 'Immediate isolation — high impact, fast containment' },
    { id: 'BALANCED',   label: 'Balanced Response',      color: '#f6ad55', desc: 'Balance containment with business continuity' },
    { id: 'CONSERVATIVE', label: 'Conservative',         color: '#63b3ed', desc: 'Minimal disruption — monitor and investigate' },
    { id: 'WATCH',      label: 'Watch and Wait',          color: '#48bb78', desc: 'Passive monitoring — collect evidence only' },
  ];

  const actions = ['Block IP/Domain', 'Isolate Endpoint', 'Disable User Account', 'Quarantine File', 'Escalate to Tier-3'];

  const execute = (action) => {
    setExecuting(true);
    const timestamp = new Date().toLocaleTimeString();
    setLog(prev => [
      { time: timestamp, msg: `[AuditLogDecorator] Pre-action audit logged`, color: '#9f7aea' },
      { time: timestamp, msg: `[ApprovalGateDecorator] Approval granted`, color: '#63b3ed' },
      { time: timestamp, msg: `[${selectedStrategy}] Executing: ${action}`, color: '#f6ad55' },
      { time: timestamp, msg: `[IncidentResponseFacade] Action dispatched to response-orchestration-service:8084`, color: '#48bb78' },
      { time: timestamp, msg: `[RollbackCapabilityDecorator] Rollback snapshot saved`, color: '#9f7aea' },
      { time: timestamp, msg: `[Observer] ResponseActionExecuted event published to EventBus`, color: '#76e4f7' },
      ...prev
    ]);
    setTimeout(() => setExecuting(false), 800);
  };

  const stratColors = { AGGRESSIVE: '#fc8181', BALANCED: '#f6ad55', CONSERVATIVE: '#63b3ed', WATCH: '#48bb78' };

  const styles = {
    title: { fontSize: '16px', fontWeight: '600', color: '#e2e8f0', marginBottom: '1.5rem' },
    grid: { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' },
    box: { background: '#1a1d2e', border: '1px solid #2d3748', borderRadius: '8px', padding: '1.2rem' },
    boxTitle: { fontSize: '12px', fontWeight: '600', color: '#718096', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '1rem' },
    stratBtn: (active, color) => ({ display: 'block', width: '100%', textAlign: 'left', padding: '10px 12px', borderRadius: '6px', border: `1px solid ${active ? color : '#2d3748'}`, background: active ? color + '11' : 'transparent', color: active ? color : '#718096', cursor: 'pointer', marginBottom: '8px' }),
    stratLabel: { fontSize: '13px', fontWeight: '600' },
    stratDesc: { fontSize: '11px', opacity: 0.7, marginTop: '2px' },
    actionBtn: { display: 'block', width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #2d3748', background: '#0f1117', color: '#e2e8f0', cursor: 'pointer', fontSize: '13px', marginBottom: '8px', textAlign: 'left' },
    log: { background: '#0f1117', borderRadius: '6px', padding: '1rem', height: '200px', overflowY: 'auto', fontFamily: 'monospace' },
    logLine: (color) => ({ fontSize: '11px', color, marginBottom: '4px' }),
  };

  return (
    <div>
      <div style={styles.title}>Response Console — Facade + Strategy + Decorator</div>
      <div style={styles.grid}>
        <div>
          <div style={styles.box}>
            <div style={styles.boxTitle}>Strategy Pattern — select response algorithm</div>
            {strategies.map(s => (
              <button key={s.id} style={styles.stratBtn(selectedStrategy===s.id, s.color)} onClick={() => setSelectedStrategy(s.id)}>
                <div style={styles.stratLabel}>{s.label}</div>
                <div style={styles.stratDesc}>{s.desc}</div>
              </button>
            ))}
          </div>
        </div>
        <div>
          <div style={styles.box}>
            <div style={styles.boxTitle}>Response Actions — via IncidentResponseFacade</div>
            {actions.map(a => (
              <button key={a} style={styles.actionBtn} onClick={() => execute(a)} disabled={executing}>
                ⚡ {a}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div style={{ marginTop: '1.5rem', ...styles.box }}>
        <div style={styles.boxTitle}>Decorator Chain Execution Log — AuditLog → Approval → Action → Rollback</div>
        <div style={styles.log}>
          {log.length === 0 && <div style={styles.logLine('#4a5568')}>// Click an action above to see the Decorator chain execute...</div>}
          {log.map((l, i) => <div key={i} style={styles.logLine(l.color)}>[{l.time}] {l.msg}</div>)}
        </div>
      </div>
    </div>
  );
}
