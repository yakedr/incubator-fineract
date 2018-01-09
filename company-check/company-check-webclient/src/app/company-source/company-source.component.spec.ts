import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanySourceComponent } from './company-source.component';

describe('CompanySourceComponent', () => {
  let component: CompanySourceComponent;
  let fixture: ComponentFixture<CompanySourceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanySourceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanySourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
