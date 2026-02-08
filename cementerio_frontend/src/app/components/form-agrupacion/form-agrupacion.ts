import { Component, OnInit, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AgrupacionService } from '../../services/agrupacionService'; 
import { ModalService } from '../../services/modalService';
import { Agrupacion } from '../../models/agrupacion';
import { Validador } from '../../validator/validador'; 

@Component({
  selector: 'app-form-agrupacion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-agrupacion.html'
})
export class FormAgrupacion implements OnInit, OnChanges {
  @Output() agrupacionCreada = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();
  @Input() idCementerioInput: any;
  @Input() agrupacionAEditar: Agrupacion | null = null;

  formAgrupacion!: FormGroup;

  constructor(
    private agrupacionService: AgrupacionService,
    private modalService: ModalService
  ) {
    this.inicializarFormulario();
  }

  ngOnInit(): void {
    if (this.agrupacionAEditar) {
      this.rellenarFormulario();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['agrupacionAEditar'] && this.formAgrupacion) {
      if (this.agrupacionAEditar) {
        this.rellenarFormulario();
      } else {
        this.resetearFormulario();
      }
    }
  }

  inicializarFormulario(): void {
    this.formAgrupacion = new FormGroup({
      id: new FormControl(null),
      nombre: new FormControl('', [Validators.required, Validators.minLength(3)]),
      tipo_forma: new FormControl('polygon', [Validators.required]),
      coordenadas_mapa: new FormControl('', [
        Validators.required]),
      filas: new FormControl(1, [Validators.required, Validators.min(1)]),
      columnas: new FormControl(1, [Validators.required, Validators.min(1)]),
      descripcion: new FormControl('')
    });


    this.formAgrupacion.get('tipo_forma')?.valueChanges.subscribe(() => {
      this.formAgrupacion.get('coordenadas_mapa')?.updateValueAndValidity();
    });
  }

  rellenarFormulario(): void {
    if (this.formAgrupacion && this.agrupacionAEditar) {
      this.formAgrupacion.patchValue(this.agrupacionAEditar);
    }
  }

  resetearFormulario(): void {
    if (this.formAgrupacion) {
      this.formAgrupacion.reset({
        tipo_forma: 'polygon',
        filas: 1,
        columnas: 1
      });
    }
  }

  isInvalid(field: string): boolean {
    const control = this.formAgrupacion.get(field);
    return !!(control && control.invalid && (control.touched || control.dirty));
  }

  getErrorMessage(field: string): string {
    const control = this.formAgrupacion.get(field);
    if (control?.hasError('required')) return 'Obligatorio.';
    if (control?.hasError('min')) return 'Mínimo 1.';
    return 'Dato no válido.';
  }

  guardar() {
    if (this.formAgrupacion.valid) {
      const datosParaEnviar = {
        ...this.formAgrupacion.value,
        id_cementerio: this.idCementerioInput 
      };

      const peticion = datosParaEnviar.id 
        ? this.agrupacionService.actualizar(datosParaEnviar.id, datosParaEnviar)
        : this.agrupacionService.crearAgrupacion(datosParaEnviar);

      peticion.subscribe({
        next: () => { 
          this.agrupacionCreada.emit(); 
          this.resetearFormulario();
        },
        error: (err) => { 
          this.modalService.mostrar('ERROR', 'No se pudo guardar el sector.');
        }
      });
    }
  }

  onCancelar() {
    this.cancelar.emit();
    this.resetearFormulario();
  }
}