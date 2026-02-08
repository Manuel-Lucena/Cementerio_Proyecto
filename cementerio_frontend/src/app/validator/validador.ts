import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export class Validador {
  
  // Teléfono: Valida números y longitud
  public static telefonoFormato(control: AbstractControl): ValidationErrors | null {
    let res: ValidationErrors | null = null;

    if (control.value) {
      const lista: string[] = [];
      if (isNaN(Number(control.value))) lista.push("Solo se permiten números.");
      if (control.value.length !== 9) lista.push("Debe tener exactamente 9 dígitos.");
      
      if (lista.length > 0) res = { erroresArray: lista };
    }

    return res;
  }

  // Fecha: Valida mayoría de edad y fecha coherente
  public static mayorEdad(control: AbstractControl): ValidationErrors | null {
    let res: ValidationErrors | null = null;

    if (control.value) {
      const lista: string[] = [];
      const fechaNac = new Date(control.value);
      const hoy = new Date();
      let edad = hoy.getFullYear() - fechaNac.getFullYear();
      
      if (hoy.getMonth() < fechaNac.getMonth() || (hoy.getMonth() === fechaNac.getMonth() && hoy.getDate() < fechaNac.getDate())) {
        edad--;
      }

      if (edad < 18) lista.push("Debes ser mayor de 18 años.");
      if (edad > 110) lista.push("La fecha no es válida.");
      
      if (lista.length > 0) res = { erroresArray: lista };
    }

    return res;
  }

  // Password: Compara coincidencia
  public static camposIguales(campo1: string, campo2: string): ValidatorFn {
    return (form: AbstractControl): ValidationErrors | null => {
      let res: ValidationErrors | null = null;
      const p1 = form.get(campo1)?.value;
      const p2 = form.get(campo2)?.value;

      if (p1 !== p2) {
        res = { coincidencia: ["Las contraseñas no coinciden."] };
      }

      return res;
    };
  }

  // Formato Coordenadas
  public static formatoCoordenadas(control: AbstractControl): ValidationErrors | null {
    let res: ValidationErrors | null = null;
    const form = control.parent;

    if (form && control.value) {
      const forma = form.get('tipo_forma')?.value;
      const valor = control.value.trim();
      const puntos = valor.split(/[\s,]+/).filter((n: string) => n !== "");

      if (forma === 'polygon') {
        if (puntos.length < 8) {
          res = { erroresArray: ["Para un sector rectangular pon las 4 esquinas (8 números)."] };
        } else if (puntos.length % 2 !== 0) {
          res = { erroresArray: ["Las coordenadas deben ser parejas x,y."] };
        }
      } else if (forma === 'circle' && puntos.length !== 3) {
        res = { erroresArray: ["El círculo requiere: centroX, centroY, radio."] };
      }
    }

    return res;
  }

  // Rango Años
  public static rangoAños(control: AbstractControl): ValidationErrors | null {
    let res: ValidationErrors | null = null;

    if (control.value) {
      const valor = Number(control.value);
      if (valor < 1 || valor > 99) {
        res = { rango: "Los años deben estar entre 5 y 99" };
      }
    }

    return res;
  }
}