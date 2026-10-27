/**
 * Smart Patient Health Monitoring System - Client Javascript
 */

document.addEventListener('DOMContentLoaded', function () {
    initLiveClock();
    initAlertPolling();
    initSidebarToggle();
});

// 1. Live Clock
function initLiveClock() {
    const clockElement = document.getElementById('liveClock');
    if (!clockElement) return;

    function update() {
        const now = new Date();
        const options = {
            weekday: 'short',
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: true
        };
        clockElement.textContent = now.toLocaleDateString(undefined, options);
    }
    update();
    setInterval(update, 1000);
}

// 2. Alert Badge Polling
function initAlertPolling() {
    const badge = document.getElementById('unreadAlertBadge');
    if (!badge) return;

    function checkAlerts() {
        fetch('/api/v1/alerts/unread-count')
            .then(res => res.json())
            .then(data => {
                const count = data.activeAlerts || 0;
                badge.textContent = count;
                if (count > 0) {
                    badge.classList.remove('d-none');
                } else {
                    badge.classList.add('d-none');
                }
            })
            .catch(err => console.debug('Alert check error:', err));
    }

    checkAlerts();
    // Poll every 15 seconds
    setInterval(checkAlerts, 15000);
}

// 3. Mobile Sidebar Toggle
function initSidebarToggle() {
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('appSidebar');

    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', function () {
            sidebar.classList.toggle('show');
        });
    }
}

// 4. Acknowledge Alert Asynchronously
async function acknowledgeAlert(alertId, btnElement) {
    if (!confirm('Mark this clinical alert as acknowledged?')) return;

    try {
        const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken && csrfHeader) {
            headers[csrfHeader] = csrfToken;
        }

        const res = await fetch(`/api/v1/alerts/acknowledge/${alertId}`, {
            method: 'POST',
            headers: headers
        });

        if (res.ok) {
            if (btnElement) {
                const row = btnElement.closest('tr') || btnElement.closest('.alert-item');
                if (row) {
                    row.style.opacity = '0.4';
                    btnElement.outerHTML = '<span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle me-1"></i>Acknowledged</span>';
                }
            }
            // Update badge
            const badge = document.getElementById('unreadAlertBadge');
            if (badge) {
                let current = parseInt(badge.textContent, 10) || 0;
                if (current > 0) badge.textContent = current - 1;
            }
        } else {
            alert('Could not acknowledge alert. Please try again.');
        }
    } catch (err) {
        console.error('Error acknowledging alert:', err);
        alert('Communication error with server.');
    }
}

// 5. Trigger Simulated Reading
async function triggerSimulation(patientId, forceAbnormal) {
    try {
        const url = `/api/v1/vitals/simulate?${patientId ? 'patientId=' + patientId : ''}&forceAbnormal=${forceAbnormal || false}`;
        const res = await fetch(url, { method: 'POST' });
        if (res.ok) {
            const data = await res.json();
            window.location.reload();
        } else {
            alert('Failed to generate simulated reading.');
        }
    } catch (err) {
        console.error(err);
        alert('Server communication error.');
    }
}
