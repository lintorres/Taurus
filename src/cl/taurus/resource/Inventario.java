package cl.taurus.resource;

public class Inventario {

	private long puntoReorden;
	private double montoSeguridad;
	private double demandaMediaPorDia;
	private long tiempoReposicion;
	private long inventarioSeguridad;
	private double consumoMinimoDiario;
	private double inventarioMaximo;
	private double cantidadReorden;
	
	public Inventario() {
	
	}
	

	public Inventario(long puntoReorden, double montoSeguridad, double demandaMediaPorDia, long tiempoReposicion,
			long inventarioSeguridad, double consumoMinimoDiario, double inventarioMaximo, double cantidadReorden) {
		super();
		this.puntoReorden = puntoReorden;
		this.montoSeguridad = montoSeguridad;
		this.demandaMediaPorDia = demandaMediaPorDia;
		this.tiempoReposicion = tiempoReposicion;
		this.inventarioSeguridad = inventarioSeguridad;
		this.consumoMinimoDiario = consumoMinimoDiario;
		this.inventarioMaximo = inventarioMaximo;
		this.cantidadReorden = cantidadReorden;
	}


	public long getPuntoReorden() {
		return puntoReorden;
	}

	public void setPuntoReorden(long puntoReorden) {
		this.puntoReorden = puntoReorden;
	}

	public double getMontoSeguridad() {
		return montoSeguridad;
	}

	public void setMontoSeguridad(double montoSeguridad) {
		this.montoSeguridad = montoSeguridad;
	}

	public double getDemandaMediaPorDia() {
		return demandaMediaPorDia;
	}

	public void setDemandaMediaPorDia(double demandaMediaPorDia) {
		this.demandaMediaPorDia = demandaMediaPorDia;
	}

	public long getTiempoReposicion() {
		return tiempoReposicion;
	}

	public void setTiempoReposicion(long tiempoReposicion) {
		this.tiempoReposicion = tiempoReposicion;
	}

	public long getInventarioSeguridad() {
		return inventarioSeguridad;
	}

	public void setInventarioSeguridad(long inventarioSeguridad) {
		this.inventarioSeguridad = inventarioSeguridad;
	}

	public double getConsumoMinimoDiario() {
		return consumoMinimoDiario;
	}

	public void setConsumoMinimoDiario(double consumoMinimoDiario) {
		this.consumoMinimoDiario = consumoMinimoDiario;
	}

	public double getInventarioMaximo() {
		return inventarioMaximo;
	}

	public void setInventarioMaximo(double inventarioMaximo) {
		this.inventarioMaximo = inventarioMaximo;
	}

	public double getCantidadReorden() {
		return cantidadReorden;
	}

	public void setCantidadReorden(double cantidadReorden) {
		this.cantidadReorden = cantidadReorden;
	}
    
	
}
