package pivLayer;

import java.util.Arrays;
import java.util.List;

public class Buffer {
	
	private ElementoProcesable[] buffer;

	public Buffer(int size) {
		this.buffer = new ElementoProcesable[size];
	}

	public Buffer(List<ElementoProcesable> buffer) {
		this.buffer = new ElementoProcesable[buffer.size()];
		this.buffer = (ElementoProcesable[]) buffer.toArray(this.buffer);
	}

	public synchronized ElementoProcesable getElem(int index) {
		while (buffer[index] == null) {
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		}
		return buffer[index];
	}

	public synchronized void putElem(int index, ElementoProcesable elem) {
		buffer[index] = elem;
		notifyAll();
	}

	public int size() {
		return buffer.length;
	}

	public synchronized void esperarCompletado() {
		boolean completo = false;
		while (!completo) {
			int i = 0;
			while (i < buffer.length && buffer[i] != null)
				i++;
			if (i != buffer.length) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else
				completo = true;
		}
	}

	public List<ElementoProcesable> getBufferList() {
		return Arrays.asList(buffer);
	}

}
