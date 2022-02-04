import { NgModule } from '@angular/core';
import {AuthModule, LogLevel} from 'angular-auth-oidc-client';


@NgModule({
    imports: [AuthModule.forRoot({
        config: {
            authority: 'https://dev-fcad9kyt.us.auth0.com',
            redirectUrl: window.location.origin,
            clientId: 'FD6wDjK4RGs4s9Nvza0msH54TQJUjbC3',
            scope: 'openid profile offline_access',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            logLevel: LogLevel.Debug,
            secureRoutes: ["http://localhost:8080/"],
            customParamsAuthRequest: {
              audience: 'http://localhost:8080'
            }
        }
      })],
    exports: [AuthModule],
})
export class AuthConfigModule {}
