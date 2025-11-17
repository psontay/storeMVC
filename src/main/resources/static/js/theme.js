document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const themeToggle = document.getElementById('themeToggle');

    if(!themeToggle) return;

    // --- Load saved theme
    const savedTheme = localStorage.getItem('storemvc-theme');
    if(savedTheme === 'dark') {
        body.classList.add('dark');
        themeToggle.checked = true;
    } else {
        body.classList.remove('dark');
        themeToggle.checked = false;
    }

    // --- Toggle event
    themeToggle.addEventListener('change', () => {
        if(themeToggle.checked){
            body.classList.add('dark');
            localStorage.setItem('storemvc-theme','dark');
        } else {
            body.classList.remove('dark');
            localStorage.setItem('storemvc-theme','light');
        }
        // Optional: trigger custom event for chart update, etc.
        window.dispatchEvent(new Event('themeChange'));
    });
});
