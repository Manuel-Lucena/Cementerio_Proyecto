import { Component, Input, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { ConcesionService } from '../../services/concesionService';
import { ModalService } from '../../services/modalService';
import { Sepultura } from '../../models/sepultura';

@Component({
  selector: 'app-form-concesion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-concesion.html',
  styleUrl: './form-concesion.scss'
})
export class FormConcesion implements OnInit {
  @Input() sepultura: Sepultura | null = null;
  @Output() onCerrar = new EventEmitter<void>();
  @Output() onExito = new EventEmitter<void>();

  concesionForm: FormGroup;
  cargando: boolean = false;

  constructor(
    private concesionService: ConcesionService,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef
  ) {
    this.concesionForm = new FormGroup({
      idSepultura: new FormControl<number | null>(null, [Validators.required]),
      fechaInicio: new FormControl(new Date().toISOString().split('T')[0], [Validators.required]),
      fechaFin: new FormControl('2029-12-31', [Validators.required]),
      totalPagar: new FormControl(500.00, [Validators.required, Validators.min(0.01)]),
      difunto: new FormGroup({
        nombre: new FormControl('', [Validators.required, Validators.minLength(2)]),
        apellidos: new FormControl('', [Validators.required, Validators.minLength(2)]),
        fechaFallecimiento: new FormControl('', [Validators.required]),
        fechaIngreso: new FormControl(new Date().toISOString().split('T')[0], [Validators.required])
      }, { validators: this.fechasCoherentes }) 
    });
  }


  fechasCoherentes(group: AbstractControl): ValidationErrors | null {
    const fallecimiento = group.get('fechaFallecimiento')?.value;
    const ingreso = group.get('fechaIngreso')?.value;

    if (fallecimiento && ingreso && fallecimiento > ingreso) {
      return { fechaInvalida: true };
    }
    return null;
  }

  ngOnInit(): void {
    if (this.sepultura) {
      this.concesionForm.patchValue({ idSepultura: this.sepultura.id });
    }
  }


  get errorFechasDifunto(): boolean {
    return this.concesionForm.get('difunto')?.hasError('fechaInvalida') && 
           this.concesionForm.get('difunto.fechaIngreso')?.touched || false;
  }

  guardar(): void {
    if (this.concesionForm.invalid) {
      this.concesionForm.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.concesionService.crearConcesion(this.concesionForm.value).subscribe({
      next: () => {
        this.cargando = false;
        this.modalService.mostrar('ÉXITO', 'Concesión y registro de difunto creados correctamente');
        this.onExito.emit();
      },
      error: (err) => {
        this.cargando = false;
        this.modalService.mostrar('ERROR', err.error?.message || 'Error al procesar el trámite');
        this.cdr.detectChanges();
      }
    });
  }
  

  cerrar(): void {
    this.onCerrar.emit();
  }
}