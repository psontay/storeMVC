/* dashboard.js
   Minimal, robust Chart.js usage with gradient cache + safe updates.
   Avoids infinite update loops by only updating when values change.
*/

document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const sidebar = document.getElementById('sidebar');
    const toggleSidebar = document.getElementById('toggleSidebar');
    const collapseBtn = document.getElementById('collapseBtn');
    const themeToggle = document.getElementById('themeToggle');

    // --- Theme load
    const savedTheme = localStorage.getItem('storemvc-theme');
    if (savedTheme === 'dark') {
        body.classList.add('dark');
        if (themeToggle) themeToggle.checked = true;
    } else {
        body.classList.remove('dark');
        if (themeToggle) themeToggle.checked = false;
    }

    // safe helper to read CSS var (returns trimmed string)
    function getCssVar(name) {
        const v = getComputedStyle(document.documentElement).getPropertyValue(name)
            || getComputedStyle(document.body).getPropertyValue(name)
            || '';
        return v.trim();
    }

    // toggle theme
    if (themeToggle) {
        themeToggle.addEventListener('change', () => {
            if (themeToggle.checked) {
                body.classList.add('dark');
                localStorage.setItem('storemvc-theme', 'dark');
            } else {
                body.classList.remove('dark');
                localStorage.setItem('storemvc-theme', 'light');
            }
            // request a single color update (debounced by microtask)
            requestAnimationFrame(() => updateChartsColors());
        });
    }

    // sidebar toggles
    if (toggleSidebar) {
        toggleSidebar.addEventListener('click', () => {
            sidebar.classList.toggle('collapsed');
        });
    }
    if (collapseBtn) {
        collapseBtn.addEventListener('click', () => sidebar.classList.toggle('collapsed'));
    }

    // --- Sample data (replace with API calls later)
    const revenueLabels = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
    const revenueData = [12000,15000,14000,17000,19000,22000,20000,24000,23000,25000,27000,30000];

    // --- Chart setup
    const revenueCanvas = document.getElementById('revenueChart');
    if (!revenueCanvas) return; // nothing to do

    const revenueCtx = revenueCanvas.getContext('2d');

    // gradient cache to avoid re-creating new gradient objects repeatedly
    let gradientCache = { color: null, w: 0, h: 0, gradient: null };

    function hexToRgba(hex, a){
        if (!hex) return `rgba(0,0,0,${a})`;
        hex = hex.trim();
        if (hex.startsWith('#') && (hex.length === 7 || hex.length === 4)) {
            // support #rgb and #rrggbb
            if (hex.length === 4) {
                const r = hex[1], g = hex[2], b = hex[3];
                hex = `#${r}${r}${g}${g}${b}${b}`;
            }
            const bigint = parseInt(hex.slice(1),16);
            const r = (bigint>>16)&255, g=(bigint>>8)&255, b=bigint&255;
            return `rgba(${r},${g},${b},${a})`;
        }
        // fallback: if it's already something like rgb(...) or color name, try to use it directly (Chart may accept)
        return hex;
    }

    function createGradient(ctx, color, alpha=0.18){
        const w = ctx.canvas.width, h = ctx.canvas.height;
        // reuse gradient if same color and size
        if (gradientCache.color === color && gradientCache.w === w && gradientCache.h === h && gradientCache.gradient) {
            return gradientCache.gradient;
        }
        const g = ctx.createLinearGradient(0,0,0,h || 300);
        g.addColorStop(0, hexToRgba(color, alpha));
        g.addColorStop(1, hexToRgba(color, 0));
        gradientCache = { color, w, h, gradient: g };
        return g;
    }

    // chart options factory
    function chartOptions() {
        const muted = getCssVar('--muted') || '#6b7280';
        return {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: { mode: 'index', intersect: false }
            },
            scales: {
                x: { grid: { display: false }, ticks: { color: muted } },
                y: { grid: { color: 'rgba(0,0,0,0.05)' }, ticks: { color: muted } }
            },
        };
    }

    // create revenue chart
    const revenueConfig = {
        type: 'line',
        data: {
            labels: revenueLabels.slice(),
            datasets: [{
                label: 'Doanh thu',
                data: revenueData.slice(),
                fill: true,
                tension: 0.3,
                backgroundColor: createGradient(revenueCtx, getCssVar('--accent') || '#FF6B3A', 0.18),
                borderColor: getCssVar('--accent') || '#FF6B3A',
                pointRadius: 3
            }]
        },
        options: chartOptions()
    };

    let revenueChart = new Chart(revenueCtx, revenueConfig);

    // Only update charts when values actually change (avoid loops)
    let lastAccent = (getCssVar('--accent') || '').trim();
    function updateChartsColors(){
        const accent = (getCssVar('--accent') || '').trim();
        if (!accent || accent === lastAccent) return; // nothing changed

        // update dataset visuals
        revenueChart.data.datasets[0].borderColor = accent;
        revenueChart.data.datasets[0].backgroundColor = createGradient(revenueCtx, accent, 0.18);

        // update ticks colors
        const muted = getCssVar('--muted') || '#6b7280';
        if (revenueChart.options && revenueChart.options.scales) {
            if (revenueChart.options.scales.x) revenueChart.options.scales.x.ticks.color = muted;
            if (revenueChart.options.scales.y) revenueChart.options.scales.y.ticks.color = muted;
        }

        lastAccent = accent;
        revenueChart.update();
    }

    // respond to range change (slice data safely)
    const revenueRange = document.getElementById('revenueRange');
    if (revenueRange) {
        revenueRange.addEventListener('change', (e) => {
            const months = Math.max(1, Math.min(12, parseInt(e.target.value,10) || 12));
            revenueChart.data.labels = revenueLabels.slice(12 - months);
            revenueChart.data.datasets[0].data = revenueData.slice(12 - months);
            revenueChart.update();
        });
    }

    // render sample recent orders table
    const recent = [
        {id: 1023, name:'Nguyễn A', total:'₫ 1,200,000', status:'pending', date:'2025-11-15'},
        {id: 1022, name:'Trần B', total:'₫ 450,000', status:'completed', date:'2025-11-14'},
        {id: 1021, name:'Lê C', total:'₫ 2,300,000', status:'pending', date:'2025-11-13'},
        {id: 1020, name:'Phạm D', total:'₫ 600,000', status:'cancel', date:'2025-11-12'},
        {id: 1019, name:'Võ E', total:'₫ 750,000', status:'completed', date:'2025-11-11'}
    ];
    const tbody = document.getElementById('recentOrders');
    if (tbody) {
        tbody.innerHTML = recent.map((r,i) => `
      <tr>
        <td>${i+1}</td>
        <td>${r.name}</td>
        <td>${r.total}</td>
        <td><span class="status ${r.status}">${r.status}</span></td>
        <td>${r.date}</td>
      </tr>
    `).join('');
    }

    // Expose functions for integration/testing
    window.adminDashboard = {
        revenueChart,
        updateChartsColors
    };

    // On resize, clear gradient cache (so gradient resizes correctly)
    let resizeTimer = null;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(() => {
            gradientCache = { color: null, w:0, h:0, gradient:null };
            // chart will re-draw automatically on resize by Chart.js, but ensure colors are correct
            updateChartsColors();
        }, 120);
    });

    // initial safe update (no infinite loop)
    requestAnimationFrame(() => updateChartsColors());
});
