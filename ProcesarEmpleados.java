import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcesarEmpleados {
  static List<Empleado> empleados;
    // Procesamiento de flujos de objetos Empleado.

    public static void main(String[] args) throws IOException{
        // inicializa arreglo de objetos Empleado
        cargarArchivo(); 
        // muestra todos los objetos Empleado
        //System.out.println("Lista completa de empleados:");
        //empleados.stream().forEach(System.out::println);
        range();
        
        // Funciones para obtener primer nombre y apellido de un Empleado
        Function<Empleado, String> porPrimerNombre = Empleado::getPrimerNombre;
        Function<Empleado, String> porApellidoPaterno = Empleado::getApellidoPaterno;

        // Comparator para comparar empleados por primer nombre y luego por apellido paterno
        Comparator<Empleado> apellidoLuegoNombre =
                Comparator.comparing(porApellidoPaterno).thenComparing(porPrimerNombre);

        // ordena empleados por apellido paterno y luego por primer nombre
        //System.out.printf(
        //        "%nEmpleados en orden ascendente por apellido y luego por nombre:%n");
        //empleados.stream()
        //        .sorted(apellidoLuegoNombre)
        //        .forEach(System.out::println);

        // ordena empleados en forma descendente por apellido, luego por nombre
        //System.out.printf(
        //        "%nEmpleados en orden descendente por apellido y luego por nombre:%n");
        //empleados.stream()
        //        .sorted(apellidoLuegoNombre.reversed())
        //        .forEach(System.out::println);
        // muestra apellidos de empleados únicos ordenados
        //System.out.printf("%nApellidos de empleados unicos:%n");
        //empleados.stream()
        //        .map(Empleado::getApellidoPaterno)
        //        .distinct()
        //        .sorted()
        //        .forEach(System.out::println);

        // muestra sólo nombre y apellido
        //System.out.printf(
        //        "%nNombres de empleados en orden por apellido y luego por nombre:%n");
        //empleados.stream()
        //        .sorted(apellidoLuegoNombre)
        //        .map(Empleado::getPrimerNombre)
        //        .forEach(System.out::println);

        // agrupa empleados por departamento
        System.out.printf("%nEmpleados por departamento:%n");
        Map<String, List<Empleado>> agrupadoPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        agrupadoPorDepartamento.forEach(
                (departamento, empleadosEnDepartamento) ->
                {
                    System.out.println(departamento+"\n Empleado que mas gana: "+empleadosEnDepartamento.stream().max(Comparator.comparing(Empleado::getSalario)).get()+"\n Nomina del departamento: "+empleadosEnDepartamento.stream().mapToDouble(Empleado::getSalario).sum());
                    empleadosEnDepartamento.forEach(
                            empleado -> System.out.printf(" %s%n", empleado));
                }
        );
        // cuenta el número de empleados en cada departamento
        System.out.printf("%nConteo de empleados por departamento:%n");
        Map<String, Long> conteoEmpleadosPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento,
                                TreeMap::new, Collectors.counting()));
        conteoEmpleadosPorDepartamento.forEach(
                (departamento, conteo) -> System.out.printf(
                        "%s tiene %d empleado(s)%n", departamento, conteo));

        // suma de salarios de empleados con el método sum de DoubleStream
        System.out.printf(
                "%nSuma de los salarios de los empleados (mediante el metodo sum): %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .sum());

        // calcula la suma de los salarios de los empleados con el método reducede Stream
        System.out.printf("Suma de los salarios de los empleados (mediante el metodo reduce): %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .reduce(0, (valor1, valor2) -> valor1 + valor2));

        // promedio de salarios de empleados con el método average de DoubleStream
        System.out.printf("Promedio de salarios de los empleados: %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .average()
                        .getAsDouble());
        // Sumatoria de Nomia
        System.out.printf("Sumatoria de Nomina: %.2f%n",
                empleados.stream().mapToDouble(Empleado::getSalario).sum());
        //Empleado que mas gana
        System.out.printf("%nEmpleado que mas gana:%n%s%n",
        empleados.stream()
                .max(Comparator.comparing(Empleado::getSalario))); 
        //Empleado que menos gana
        System.out.printf("%nEmpleado que menos gana:%n%s%n",
        empleados.stream()
                .min(Comparator.comparing(Empleado::getSalario)));
    }
    
    static void cargarArchivo() throws IOException{
      Pattern pattern= Pattern.compile( ";" );
      try(Stream<String> lines =Files.lines(Path.of("empleado.csv"))){
        empleados=lines.skip(1).map(line -> {
          String[] arr = pattern.split(line);
          return new Empleado(arr[0],arr[1],arr[2],Double.parseDouble(arr[3]),arr[4]);
        }).collect(Collectors.toList());
      }
    }
    static void range(){
        // Predicado que devuelve true para salarios en el rango $4000-$6000
        Predicate<Empleado> cuatroASeisMil =e -> (e.getSalario() >= 4000 && e.getSalario() <= 6000);

        // Muestra los empleados con salarios en el rango $4000-$6000
        // en orden ascendente por salario
        System.out.printf(
                "%nEmpleados que ganan $4000-$6000 mensuales ordenados por salario:%n");
        empleados.stream()
                .filter(cuatroASeisMil)
                .sorted(Comparator.comparing(Empleado::getSalario))
                .forEach(System.out::println);

        // Muestra el primer empleado con salario en el rango $4000-$6000
        System.out.printf("%nPrimer empleado que gana $4000-$6000:%n%s%n",
                empleados.stream()
                        .filter(cuatroASeisMil)
                        .findFirst()
                        .get());
    }
} 