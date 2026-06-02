import React, { useState, useEffect } from 'react';

// MVC: View — real-time alert stream
// PATTERN: Observer — view updates when AlertIngested events arrive
// SERVICE: alert-ingestion-service:8081
export default function AlertStreamView() {
  const [alerts, setAlerts] = useState([]);
  const [filter, setFilter] = useState('ALL');

  const mockAlerts = [
    { id: 'ALT-001', source: 'Splunk', severity: 'CRITICAL', sourceIp: '203.0.113.42', eventType: 'Brute Force', time: '10:42:31', normalizer: 'SplunkNormalizer' },
    { id: 'ALT-002', source: 'CrowdStrike', severity: 'HIGH', sourceIp: '198.51.100.7', eventType: 'Malware Detected', time: '10:41:18', normalizer: 'CrowdStrikeNormalizer' },
    { id: 'ALT-003', source: 'Firewall', severity: 'MEDIUM', sourceIp: '10.0.0.55', eventType: 'Port Scan', time: '10:40:05', normalizer: 'FirewallNormalizer' },
    { id: 'ALT-004', source: 'CloudSIEM', severity: 'HIGH', sourceIp: '172.16.0.23', eventType: 'Privilege Escalation', time: '10:39:52', normalizer: 'CloudSIEMNormalizer' },
    { id: 'ALT-005', source: 'Splunk', severity: 'LOW', sourceIp: '192.168.1.10', eventType: 'Failed Login', time: '10:38:44', normalizer: 'SplunkNormalizer' },
    { id: 'ALT-006', source: 'Firewall', severity: 'CRITICAL', sourceIp: '203.0.113.99', eventType: 'DDoS Pattern', time: '10:37:30', normalizer: 'FirewallNormalizer' },
  ];

  useEffect(() => {
    // PATTERN: Observer — simulate SSE push from alert-ingestion-service
    let i = 0;
    const interval = setInterval(() => {
      if (i < mockAlerts.length) { setAlerts(prev => [mockAlerts[i], ...prev]); i++; }
    }, 600);
    return () => clearInterval(interval);
  }, []);

  const severityColor = { CRITICAL: '#fc8181', HIGH: '#f6ad55', MEDIUM: '#63b3ed', LOW: '#48bb78' };
  const filtered = filter === 'ALL' ? alerts : alerts.filter(a => a.severity === filter);

  const styles = {
    header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' },
    title: { fontSize: '16px', fontWeight: '600', color: '#e2e8f0' },
    filters: { display: 'flex', gap: '8px' },
    filterBtn: (active, color) => ({
      padding: '4px 12px', borderRadius: '4px', border: `1px solid ${color}`,
      background: active ? color + '33' : 'transparent', color: active ? color : '#718096',
      cursor: 'pointer', fontSize: '12px', fontWeight: '500'
    }),
    table: { width: '100%', borderCollapse: 'collapse' },
    th: { textAlign: 'left', padding: '10px 12px', fontSize: '11px', color: '#4a5568', textTransform: 'uppercase', borderBottom: '1px solid #2d3748' },
    td: { padding: '12px', fontSize: '13px', color: '#cbd5e0', borderBottom: '1px solid #1a1d2e' },
    badge: (color) => ({ background: color + '22', color, padding: '2px 8px', borderRadius: '3px', fontSize: '11px', fontWeight: '600' }),
    source: { fontSize: '11px', color: '#4a5568', marginTop: '2px' },
  };

  return (
    <div>
      <div style={styles.header}>
        <div style={styles.title}>Alert Stream — Factory Method (4 normalizers active)</div>
        <div style={styles.filters}>
          {['ALL','CRITICAL','HIGH','MEDIUM','LOW'].map(f => (
            <button key={f} style={styles.filterBtn(filter===f, severityColor[f]||'#718096')} onClick={() => setFilter(f)}>{f}</button>
          ))}
        </div>
      </div>
      <table style={styles.table}>
        <thead>
          <tr>
            <th style={styles.th}>ID</th>
            <th style={styles.th}>Severity</th>
            <th style={styles.th}>Source / Normalizer</th>
            <th style={styles.th}>Source IP</th>
            <th style={styles.th}>Event Type</th>
            <th style={styles.th}>Time</th>
          </tr>
        </thead>
        <tbody>
          {filtered.map(alert => (
            <tr key={alert.id}>
              <td style={styles.td}>{alert.id}</td>
              <td style={styles.td}><span style={styles.badge(severityColor[alert.severity])}>{alert.severity}</span></td>
              <td style={styles.td}>{alert.source}<div style={styles.source}>{alert.normalizer}</div></td>
              <td style={styles.td}>{alert.sourceIp}</td>
              <td style={styles.td}>{alert.eventType}</td>
              <td style={styles.td}>{alert.time}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
