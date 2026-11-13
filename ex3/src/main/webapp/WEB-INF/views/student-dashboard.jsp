<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Statistics Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <%-- ===== EXERCISE 3 - PART 2: STATISTICS DASHBOARD (NEW) ===== --%>
    <div class="container">
        <div class="page-header">
            <h1>Statistics Dashboard</h1>
            <a href="student?action=list" class="btn btn-secondary">Back to Student List</a>
        </div>

        <section class="stat-card">
            <span class="stat-label">Total Students</span>
            <strong class="stat-number">${totalStudents}</strong>
        </section>

        <div class="chart-grid">
            <section class="chart-card">
                <h2>Students by Major</h2>
                <div class="chart-wrapper">
                    <canvas id="majorChart"></canvas>
                </div>
            </section>

            <section class="chart-card">
                <h2>Students Added per Month - Last 6 Months</h2>
                <div class="chart-wrapper">
                    <canvas id="growthChart"></canvas>
                </div>
            </section>
        </div>
    </div>

    <script>
        new Chart(document.getElementById('majorChart'), {
            type: 'pie',
            data: {
                labels: ${majorLabels},
                datasets: [{
                    data: ${majorCounts},
                    backgroundColor: ['#007bff', '#28a745', '#ffc107', '#dc3545', '#6f42c1', '#17a2b8']
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });

        new Chart(document.getElementById('growthChart'), {
            type: 'bar',
            data: {
                labels: ${monthLabels},
                datasets: [{
                    label: 'Students Added',
                    data: ${monthCounts},
                    backgroundColor: '#007bff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
            }
        });
    </script>
    <%-- ===== END EXERCISE 3 - PART 2 ===== --%>
</body>
</html>
