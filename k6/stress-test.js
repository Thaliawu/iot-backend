import http from 'k6/http';
import { check } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 10 },
        { duration: '2m', target: 50 },
        { duration: '1m', target: 50 },
        { duration: '30s', target: 0 },
    ],
};

export default function () {
    const res = http.get('http://localhost:30080/kafka/send?msg=stress_test');
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
