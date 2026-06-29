import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

const API = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class FlightService {
  constructor(private http: HttpClient) {}

  getFlights(date?: string): Observable<any[]> {
    let params = new HttpParams();
    if (date) params = params.set('date', date);
    return this.http.get<any[]>(`${API}/flights`, { params });
  }

  getFlightDetail(id: number): Observable<any> {
    return this.http.get<any>(`${API}/flights/${id}/detail`);
  }

  getRecommendation(id: number): Observable<any> {
    return this.http.get<any>(`${API}/flights/${id}/recommendation`);
  }

  getConditions(id: number): Observable<any> {
    return this.http.get<any>(`${API}/flights/${id}/conditions`);
  }

  getAccumulateReport(): Observable<any> {
    return this.http.get<any>(`${API}/flights/accumulate-report`);
  }

  getCepStatus(id: number): Observable<any> {
    return this.http.get<any>(`${API}/flights/${id}/cep-status`);
  }

  sendWeatherEvent(id: number, windSpeed: number, visibility: number, temperature: number): Observable<any> {
    const params = new HttpParams()
      .set('windSpeed', windSpeed)
      .set('visibility', visibility)
      .set('temperature', temperature);
    return this.http.post<any>(`${API}/cep/${id}/weather`, null, { params });
  }

  sendAlarmEvent(id: number, severity: string): Observable<any> {
    const params = new HttpParams().set('severity', severity);
    return this.http.post<any>(`${API}/cep/${id}/alarm`, null, { params });
  }

  createFlight(body: any): Observable<any> {
    return this.http.post<any>(`${API}/flights`, body);
  }

  updateFlight(id: number, body: any): Observable<any> {
    return this.http.put<any>(`${API}/flights/${id}`, body);
  }

  deleteFlight(id: number): Observable<any> {
    return this.http.delete<any>(`${API}/flights/${id}`);
  }
}
