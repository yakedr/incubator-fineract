import {environment} from '../environments/environment';
import {Status} from "./interfaces/status";

if (environment.production) {
  this.serverUrl = 'http://api.innovaip.com:8081/api/v1';
} else {
  this.serverUrl = 'http://localhost:8081/api/v1'; //'http://localhost:8080/company-module-0.0.1-SNAPSHOT/api/v1';
}

export const configApp = {
  serverUrl: this.serverUrl,
  paginator: {
    limit: 10
  },
  statusOptions: [
    new Status('Aprobado','200','APPROVED'),
    new Status('Rechazado', '500', 'REJECTED'),
    new Status('Pendiente', '100', 'PENDING_APPROVAL'),
    new Status('Inválido', '0', 'INVALID')
  ]
};


