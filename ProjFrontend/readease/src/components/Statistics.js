import React, { useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);


const EbookReservationStats = () => {
    const [reservations, setReservations] = useState([]);
    const [chartData, setChartData] = useState(null); // Initialize to null

    useEffect(() => {
        fetch('http://localhost:8080/EbookReservations/all')
            .then(response => response.json())
            .then(data => {
                setReservations(data);
                processChartData(data);
            })
            .catch(error => console.error('Error fetching data:', error));
    }, []);

    const options = {
        scales: {
            x: {
                type: 'category', // Correctly set the x-axis scale type
            },
            y: {
                type: 'linear', // y-axis scale type
                beginAtZero: true
            }
        },
    };

    const processChartData = (data) => {
        const countPerDay = data.reduce((acc, reservation) => {
            const date = new Date(reservation.startTime).toLocaleDateString();
            acc[date] = (acc[date] || 0) + 1;
            return acc;
        }, {});

        const labels = Object.keys(countPerDay);
        const counts = Object.values(countPerDay);

        setChartData({
            labels,
            datasets: [{
                label: 'Reservations per Day',
                data: counts,
                backgroundColor: 'rgba(54, 162, 235, 0.6)'
            }]
        });
    };

    return (
        <div className='statistics-page'>
            <h2>Ebook Reservations Statistics</h2>
            <div className='chart-container'>
                {chartData && <Bar data={chartData} options={options} />}
            </div>
        </div>
    );
};

export default EbookReservationStats;