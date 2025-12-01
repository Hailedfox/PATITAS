
## citas scheme
```
create table public.citas (
    idcitas bigint generated always as identity not null,
    nombre_cliente text not null,
    numero_emergencia text not null,
    mascota_nombre text not null,
    servicio_nombre text not null,
    fecha date not null,
    hora text not null,
    estatus public.estatus_cita not null default 'Programada'::estatus_cita,
    id_cliente integer null,
    constraint citas_pkey primary key (idcitas),
    constraint citas_id_cliente_fkey foreign KEY (id_cliente) references cliente (idcliente) on update CASCADE on delete CASCADE
) TABLESPACE pg_default;
```