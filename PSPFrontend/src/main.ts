import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http'; // <-- ovo
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    ...(appConfig.providers || []), // zadržava postojeće providere
    provideHttpClient(), // <-- ovo omogućava HttpClient u servisima
  ]
})
.catch((err) => console.error(err));
