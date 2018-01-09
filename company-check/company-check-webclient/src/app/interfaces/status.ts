export class Status {
  name?: string;
  code?: string;
  value?: string;

  constructor(name: string, code:string, value: string){
    this.name = name;
    this.code = code;
    this.value = value;
  }
}
