-- Вивести вартість кожного проєкту
SELECT
    pr.ID AS PROJECT_ID,
    SUM(w.SALARY * DATEDIFF(pr.FINISH_DATE, pr.START_DATE)) AS PRICE
FROM
    project pr
JOIN project_worker pw ON pr.ID = pw.PROJECT_ID
JOIN worker w ON pw.WORKER_ID = w.ID
GROUP BY
    pr.ID
ORDER BY
    PRICE DESC;