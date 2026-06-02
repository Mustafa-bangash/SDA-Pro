import React, { useState, useEffect } from 'react';

// MVC: View — SOC metrics overview
// PATTERN: Observer — metrics update when events arrive from EventBusPublisher
// ARCHITECTURE: Layered — View → Model → Service → API
export default function MetricsDashboardView() {
  const [metrics, setMetrics] = useState({
    totalAlerts: 0, openIncidents: 0, resolvedToday: 0,
    criticalAlerts: 0, avgResponseTime: 0, threatsBlocked: 0
  });

  useEffect(() => {
    // Simulate Observer receiving real-time metrics from EventBusPublisher
    // In production: connects to SSE endpoint at audit-service:8087
    const timer = setTimeout(() => {
      setMetrics({ totalAlerts: 1247, openIncidents: 8, resolvedToday: 23,
                   criticalAlerts: 3, avgResponseTime: 4.2, threatsBlocked: 156 });
    }, 800);
    return () => clearTimeout(timer);
  }, []);

  const card = (label, value, color, sub) => ({
    card: {
      background: '#1a1d2e', border: `1px solid ${color}33`,
      borderRadius: '8px', padding: '1.2rem 1.5rem',
      borderLeft: `3px solid ${color}`
    },
    label: { fontSize: '11px', color: '#718096', textTransform: 'uppercase', letterSpacing: '0.05em' },
    value: { fontSize: '28px', fontWeight: '700', color, margin: '4px 0' },
    sub: { fontSize: '11px', color: '#4a5568' }
  });

  const statCards = [
    { label: 'Total Alerts', value: metrics.totalAlerts.toLocaleString(), color: '#63b3ed', sub: 'Last 24 hours' },
    { label: 'Open Incidents', value: metrics.openIncidents, color: '#fc8181', sub: 'Require attention' },
    { label: 'Resolved Today', value: metrics.resolvedToday, color: '#48bb78', sub: 'Closed incidents' },
    { label: 'Critical Alerts', value: metrics.criticalAlerts, color: '#f6ad55', sub: 'Immediate action' },
    { label: 'Avg Response (min)', value: metrics.avgResponseTime, color: '#9f7aea', sub: 'MTTR target: <5 min' },
    { label: 'Threats Blocked', value: metrics.threatsBlocked, color: '#76e4f7', sub: 'Auto-contained' },
  ];

  const styles = {
    title: { fontSize: '16px', fontWeight: '600', color: '#e2e8f0', marginBottom: '1.5rem' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '1rem', marginBottom: '2rem' },
    pipelineBox: { background: '#1a1d2e', border: '1px solid #2d3748', borderRadius: '8px', padding: '1.5rem' },
    pipelineTitle: { fontSize: '13px', fontWeight: '600', color: '#a0aec0', marginBottom: '1rem' },
    step: { display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '10px' },
    stepDot: (color) => ({ width: '10px', height: '10px', borderRadius: '50%', background: color, flexShrink: 0 }),
    stepLabel: { fontSize: '13px', color: '#cbd5e0' },
    stepCount: (color) => ({ marginLeft: 'auto', fontSize: '12px', fontWeight: '600', color }),
    arrow: { color: '#4a5568', fontSize: '16px', marginLeft: '1rem' }
  };

  const pipeline = [
    { label: 'Alert Ingestion (Splunk, CrowdStrike, Firewall, CloudSIEM)', color: '#63b3ed', count: '1,247', pattern: 'Factory Method' },
    { label: 'Deduplication Handler', color: '#9f7aea', count: '1,089', pattern: 'Chain of Responsibility' },
    { label: 'GeoIP + ThreatIntel Enrichment', color: '#f6ad55', count: '1,089', pattern: 'Chain of Responsibility' },
    { label: 'Classification (Composite grouping)', color: '#fc8181', count: '847', pattern: 'Composite + Strategy' },
    { label: 'Incident Creation', color: '#48bb78', count: '8', pattern: 'State Machine' },
    { label: 'Response Orchestration', color: '#76e4f7', count: '5', pattern: 'Facade + Decorator' },
  ];

  return (
    <div>
      <div style={styles.title}>SOC Overview — Real-time Metrics</div>
      <div style={styles.grid}>
        {statCards.map(s => {
          const st = card(s.label, s.value, s.color, s.sub);
          return (
            <div key={s.label} style={st.card}>
              <div style={st.label}>{s.label}</div>
              <div style={st.value}>{s.value}</div>
              <div style={st.sub}>{s.sub}</div>
            </div>
          );
        })}
      </div>

      <div style={styles.pipelineBox}>
        <div style={styles.pipelineTitle}>Alert Processing Pipeline — Chain of Responsibility</div>
        {pipeline.map((step, i) => (
          <div key={i} style={styles.step}>
            <div style={styles.stepDot(step.color)}></div>
            <span style={styles.stepLabel}>{step.label}</span>
            <span style={{ fontSize: '10px', color: '#4a5568', marginLeft: '6px' }}>({step.pattern})</span>
            <span style={styles.stepCount(step.color)}>{step.count}</span>
            {i < pipeline.length - 1 && <span style={styles.arrow}>↓</span>}
          </div>
        ))}
      </div>
    </div>
  );
}
