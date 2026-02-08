import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal-confirmacion',
  standalone: true, 
  imports: [CommonModule],
  templateUrl: './confirmacion.html',
  styleUrl: './confirmacion.scss'
})
export class Confirmacion {
  @Input() titulo: string = 'Confirmar eliminación';
  @Input() mensaje: string = '¿Estás seguro de que deseas eliminar este registro?';
  @Input() textoBotonConfirmar: string = 'ELIMINAR';

  @Output() confirmado = new EventEmitter<void>();
  @Output() cancelado = new EventEmitter<void>();

  confirmar() {
    this.confirmado.emit();
  }

  cancelar() {
    this.cancelado.emit();
  }
}