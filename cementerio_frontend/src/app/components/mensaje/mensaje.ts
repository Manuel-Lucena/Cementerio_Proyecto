import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalService } from '../../services/modalService';

@Component({
  selector: 'app-mensajes-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mensaje.html',
  styleUrls: ['./mensaje.scss']
})
export class MensajesModalComponent implements OnInit {
  visible = false;
  titulo = '';
  mensaje = '';

  constructor(private modalService: ModalService) {}

  ngOnInit() {
    this.modalService.modalState$.subscribe(state => {
      this.visible = state.visible;
      this.titulo = state.titulo;
      this.mensaje = state.mensaje;
    });
  }

  cerrar() {
    this.modalService.cerrar();
  }
}