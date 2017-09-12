package pivLayer;

import java.util.ArrayList;

import java.util.List;


public abstract class Procesador{

	protected List<FiltroProcesable> filtros;
	protected List<Seleccionador> seleccionadores;

	public Buffer procesar(Buffer input) throws FilterException {
		List<Buffer> bufferList = procesarList(input);
		return bufferList.get(bufferList.size() - 1);
	}

	public List<Buffer> procesarList(Buffer input) throws FilterException {
		List<Buffer> bufferList = new ArrayList<Buffer>();
		bufferList.add(input);
		Buffer inputF = input;
		Buffer output = null;
		for (int i = 0; i < filtros.size(); i++) {
			int cantProcesosFiltro = seleccionadores.get(i).cantIteraciones(input.size(), filtros.get(i).getCantElementosProcesables());

			output = new Buffer(cantProcesosFiltro * filtros.get(i).getCantElementosGenerados());
			bufferList.add(output);
			for (int iteracion = 0; iteracion < cantProcesosFiltro; iteracion++) {
				Proceso p = new Proceso(filtros.get(i), inputF, output, seleccionadores.get(i), iteracion);
				p.start();
			}

			inputF = output;
		}
		return bufferList;
	}	

}
