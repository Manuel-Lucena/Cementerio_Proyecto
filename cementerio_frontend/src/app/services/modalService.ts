import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface ModalData {
  titulo: string;
  mensaje: string;
  visible: boolean;
}

@Injectable({ providedIn: 'root' })
export class ModalService {
  private modalState = new BehaviorSubject<ModalData>({ titulo: '', mensaje: '', visible: false });
  modalState$ = this.modalState.asObservable();

  mostrar(titulo: string, mensaje: string) {
    this.modalState.next({ titulo, mensaje, visible: true });
  }

  cerrar() {
    this.modalState.next({ ...this.modalState.value, visible: false });
  }
}