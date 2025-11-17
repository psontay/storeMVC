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

    // helper
    function getCssVar(name) {
        return (getComputedStyle(document.documentElement).getPropertyValue(name) || '').trim();
    }

    // theme toggle
    if (themeToggle) {
        themeToggle.addEventListener('change', () => {
            body.classList.toggle('dark', themeToggle.checked);
            localStorage.setItem('storemvc-theme', themeToggle.checked ? 'dark' : 'light');
            requestAnimationFrame(() => updateChartsColors());
        });
    }

    // sidebar toggle
    if (toggleSidebar) toggleSidebar.addEventListener('click', () => sidebar.classList.toggle('collapsed'));
    if (collapseBtn) collapseBtn.addEventListener('click', () => sidebar.classList.toggle('collapsed'));

    // --- Chart setup
    const revenueCanvas = document.getElementById('revenueChart');
    if (!revenueCanvas) return;

    const revenueCtx = revenueCanvas.getContext('2d');
    let gradientCache = { color: null, w: 0, h: 0, gradient: null };

    function hexToRgba(hex, a){
        if (!hex) return `rgba(0,0,0,${a})`;
        hex = hex.trim();
        if (hex.startsWith('#') && (hex.length === 7 || hex.length === 4)) {
            if (hex.length === 4) {
                const r = hex[1], g = hex[2], b = hex[3];
                hex = `#${r}${r}${g}${g}${b}${b}`;
            }
            const bigint = parseInt(hex.slice(1),16);
            const r = (bigint>>16)&255, g=(bigint>>8)&255, b=bigint&255;
            return `rgba(${r},${g},${b},${a})`;
        }
        return hex;
    }

    function createGradient(ctx, color, alpha=0.18){
        const w = ctx.canvas.width, h = ctx.canvas.height;
        if (gradientCache.color === color && gradientCache.w === w && gradientCache.h === h && gradientCache.gradient) {
            return gradientCache.gradient;
        }
        const g = ctx.createLinearGradient(0,0,0,h || 300);
        g.addColorStop(0, hexToRgba(color, alpha));
        g.addColorStop(1, hexToRgba(color, 0));
        gradientCache = { color, w, h, gradient: g };
        return g;
    }

    function chartOptions() {
        const muted = getCssVar('--muted') || '#6b7280';
        return {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false }, tooltip: { mode: 'index', intersect: false } },
            scales: {
                x: { grid: { display: false }, ticks: { color: muted } },
                y: { grid: { color: 'rgba(0,0,0,0.05)' }, ticks: { color: muted } }
            },
        };
    }

    // init empty chart
    const revenueConfig = {
        type: 'line',
        data: { labels: [], datasets: [{ label: 'Doanh thu', data: [], fill: true, tension: 0.3, backgroundColor: '', borderColor: '', pointRadius: 3 }] },
        options: chartOptions()
    };
    let revenueChart = new Chart(revenueCtx, revenueConfig);

    let lastAccent = getCssVar('--accent') || '';
    function updateChartsColors(){
        const accent = getCssVar('--accent') || '';
        if (!accent || accent === lastAccent) return;
        revenueChart.data.datasets[0].borderColor = accent;
        revenueChart.data.datasets[0].backgroundColor = createGradient(revenueCtx, accent, 0.18);
        const muted = getCssVar('--muted') || '#6b7280';
        if (revenueChart.options.scales) {
            if (revenueChart.options.scales.x) revenueChart.options.scales.x.ticks.color = muted;
            if (revenueChart.options.scales.y) revenueChart.options.scales.y.ticks.color = muted;
        }
        lastAccent = accent;
        revenueChart.update();
    }

    // load KPI hôm nay
    async function loadTodayRevenue() {
        try {
            const res = await fetch('/admin/revenueToday');
            const json = await res.json();
            const value = json.todayRevenue || 0;
            document.getElementById('kpi-revenue').textContent = value.toLocaleString('vi-VN', { style:'currency', currency:'VND' });
        } catch(e){ console.error(e); }
    }

    // load 12 tháng revenue cho chart
    async function loadRevenue12Months() {
        try {
            const res = await fetch('/admin/revenue12Months');
            const json = await res.json();
            revenueChart.data.labels = json.labels || [];
            revenueChart.data.datasets[0].data = json.data || [];
            const accent = getCssVar('--accent') || '#FF6B3A';
            revenueChart.data.datasets[0].borderColor = accent;
            revenueChart.data.datasets[0].backgroundColor = createGradient(revenueCtx, accent, 0.18);
            revenueChart.update();
        } catch(e){ console.error(e); }
    }

    // reload on page load
    loadTodayRevenue();
    loadRevenue12Months();
    requestAnimationFrame(() => updateChartsColors());

    window.addEventListener('resize', () => {
        gradientCache = { color: null, w:0, h:0, gradient:null };
        updateChartsColors();
    });
});
