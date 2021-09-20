import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveVideoDetailsComponent } from './save-video-details.component';

describe('SaveVideoDetailsComponent', () => {
  let component: SaveVideoDetailsComponent;
  let fixture: ComponentFixture<SaveVideoDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SaveVideoDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaveVideoDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
