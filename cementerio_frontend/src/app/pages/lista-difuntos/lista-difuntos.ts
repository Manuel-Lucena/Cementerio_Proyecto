import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Navbar } from '../../components/navbar/navbar';
import { Footer } from '../../components/footer/footer';
import { DifuntoService } from '../../services/difuntoService';
import { Difunto } from '../../models/difunto';
import { Router } from '@angular/router';
import { FormContratarServicios } from '../../components/form-contratar-servicios/form-contratar-servicios';

@Component({
  selector: 'app-lista-difuntos',
  standalone: true,
  imports: [CommonModule, Navbar, Footer, FormContratarServicios],
  templateUrl: './lista-difuntos.html',
  styleUrl: './lista-difuntos.scss',
})
export class ListaDifuntos implements OnInit {
  misDifuntos: Difunto[] = [];
  cargando: boolean = true;
  

  mostrarModal: boolean = false;
  difuntoSeleccionado!: Difunto;

  constructor(
    private difuntoService: DifuntoService,
    private router: Router,
    private cdr: ChangeDetectorRef 
  ) { }

  ngOnInit(): void {
    this.cargarDifuntos();
  }

  cargarDifuntos(): void {
    this.cargando = true;
    this.difuntoService.getMisDifuntos().subscribe({
      next: (res: Difunto[]) => {
        this.misDifuntos = res;
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error:', err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  irAContratar(difunto: Difunto): void {
    this.difuntoSeleccionado = difunto;
    this.mostrarModal = true;
    this.cdr.detectChanges();
  }

  procesarContratacion(servicios: any[]): void {
    console.log('Servicios para el backend:', servicios);
    this.mostrarModal = false;
    this.cdr.detectChanges();
  }
}