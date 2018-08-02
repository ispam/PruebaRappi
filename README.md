
# Prueba Rappi
Prueba de conocimiento para entrevista de trabajo.

**Objetivo:** Construir una aplicacion que consuma una API de peliculas y mostrar su informacion en la aplicacion.

**Deadline:** 72 horas a partir de Julio 30 al medio dia.
## Arquitectura

**1. Las capas de la aplicación (por ejemplo capa de persistencia, vistas, red, negocio, etc) y qué clases pertenecen a cual.**

![1](https://github.com/ispam/PruebaRappi/blob/master/screens/arch.jpg)

Las capas de esta aplicación están presentadas de la siguiente forma:
-	*Capa de Presentación (Presentation Layer):* Esta incluye todos los componentes relacionados con la UI. (Activity, View, Presenters)
-	*Capa de Dominio (Domain Layer):* También conocidos como los interactores; son una extensión de la lógica del negocio. Son utilizados para satisfacer los casos de uso de las reglas del negocio. Ej: Transferir dinero de una cuenta a otra (Nombre de Clase = “TransferMoney”)
-	*Capa de Información (Data Layer):* Su función primordial es servirle a las entidades como un “output” para esas partes de la aplicación que quieren usar operaciones CRUD, también pueden hacer relaciones mucho más complejas como: filtros, concatenación, mapping,  etc.



**2. La responsabilidad de cada clase creada.**

![2](https://github.com/ispam/PruebaRappi/blob/master/screens/Modules.jpg)

Al criterio de la imagen, los paquetes estan divididos por componentes.

**Activities:** Aquí estan las clases responsables de recibir los inputs de las views y mostrar la información correspondiente.
-	*App:* Superclass que contiene la inyección de DaggerAppComponent de Dagger2.
-	*DetailsActivity:* Clase encargada de mostrar a un nivel más preciso la información de la entidad Movie.
-	*MainActivity:* Clase principal de la aplicación, contiene la lista de películas y varios métodos de funcionamiento. (Endless scrolling, Search, Categories)
-	*SplashScreenActivity:* Actividad tipo splash, comúnmente utilizada para presentar el logo y/o cargar contenido desde un servidor.

**Adapters:** Son el puente entre la información a mostrar y su vista.
-	*MoviesAdapter:* Adapter principal, encargada de organizar y conectar la información con sus respectivos fields.
-	*MoviesCursorAdapter:* Adapter secundario, encargado de inflar un layout sencillo y mostrar el query de las búsquedas.

**DI(Dependency Injection):** Grafico utilizado para usar correctamente la librería Dagger2.
-	*AppComponent:* El puente entre un servicio y un cliente.
-	*AppModule:* Modulo del contexto de la aplicación.
-	*NetworkModule:* Modulo para la conexión de servicios HTTP con la aplicación.
-	*ViewModelModule:* Modulo para la inyección de los ViewModels con sus respectivos clientes.

**Repository:** Proporciona información de multiples fuentes (DB, API)  he aísla la capa de datos.
-	*MoviesRepository:* Clase principal para la conexión y extracion de información del cache o de algún sitio remoto.

**Local:**
-	*DAOs:* Objecto o Interface que provee acceso a la base de datos sin exponer los detalles de la misma.
-	*Entities:* Objetos de dominio u objetos comerciales, son el núcleo de la aplicación. ellos representan la funcionalidad principal de la aplicación. Ellos controlan la lógica de negocios.
-	*ViewModels:* Tipo de arquitectura comúnmente utilizado en Android por permitir la separación de la UI con la lógica de negocios, su objetivo principal es dejar el código de la UI simple y libre de la lógica de la app.
-	*PruebaRappiDB:* Clase abstracta para la creación de la base de datos Room, es la que sirve de puente entres los DAOs y los Presenters o VM.

**Remote:**
-	*API:* Clase encargada de hacer requests tipo HTTP hacia algún servidor. Se utiliza retrofit.


## Preguntas

**1. En qué consiste el principio de responsabilidad única? Cuál es su propósito?**

Es la S del acrónimo de S.O.L.I.D, muy conocido en el mundo de la programación orientada a objetos por ser responsable de un y solo un actor de la aplicación, también debe ser encapsulado por la clase correspondiente.
Su propósito es separar el código de diferentes actores de los cuales se es dependiente.

**2. Qué características tiene, según su opinión, un “buen” código o código limpio?**

Para mí un código limpio es aquel que es tan claro que no se necesita comentar mucho para que se entienda. También utilizar buenos indents, separar métodos por espacios, usar camelCase (Opinión personal, pero he utilizado otros tipos), usar los métodos más apropiados que contenga el lenguaje, NO reinventar la rueda.


## Librerias
- [Room](https://developer.android.com/topic/libraries/architecture/room.html) (Android Architecture Components) by Google
- [RxJava](https://github.com/ReactiveX/RxJava) by ReactiveX
- [Dagger](https://github.com/google/dagger) by Google
- [Retrofit](https://github.com/square/retrofit) by Square
- [okHTTP](https://github.com/square/okhttp) by Square
- [Picasso](https://github.com/square/picasso) by Square
- [LoadingView](https://github.com/ldoublem/LoadingView) by ldoublem

## Licencia

Copyright [2018] [Diego Fernando Urrea Gutiérrez]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.