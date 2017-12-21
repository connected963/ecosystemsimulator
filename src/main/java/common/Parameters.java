package main.java.common;

public class Parameters {

    private Integer numeroTamanduas;

    private Integer numeroFormigas;

    private Integer numeroCalorias;

    private Integer tempoDecrementoCalorias;

    private Integer caloriasReproducaoFormigas;

    private Integer caloriasRemovidasReproducaoFormigas;

    private Integer tempoGeracaoAcucares;

    private Integer movimentosPorDirecao;

    private Integer incrementoPorIngestao;

    private Integer intervaloReproducaoTamandua;

    private Parameters() {

    }

    public static Parameters getInstance() {
        return ParemetersInstance.INSTANCE;
    }

    private static class ParemetersInstance {
        private static final Parameters INSTANCE = new Parameters();

        private ParemetersInstance() {
        }
    }

    public Integer getNumeroTamanduas() {
        return numeroTamanduas;
    }

    public void setNumeroTamanduas(Integer numeroTamanduas) {
        this.numeroTamanduas = numeroTamanduas;
    }

    public Integer getNumeroFormigas() {
        return numeroFormigas;
    }

    public void setNumeroFormigas(Integer numeroFormigas) {
        this.numeroFormigas = numeroFormigas;
    }

    public Integer getNumeroCalorias() {
        return numeroCalorias;
    }

    public void setNumeroCalorias(Integer numeroCalorias) {
        this.numeroCalorias = numeroCalorias;
    }

    public Integer getTempoDecrementoCalorias() {
        return tempoDecrementoCalorias;
    }

    public void setTempoDecrementoCalorias(Integer tempoDecrementoCalorias) {
        this.tempoDecrementoCalorias = tempoDecrementoCalorias;
    }

    public Integer getCaloriasReproducaoFormigas() {
        return caloriasReproducaoFormigas;
    }

    public void setCaloriasReproducaoFormigas(Integer caloriasReproducaoFormigas) {
        this.caloriasReproducaoFormigas = caloriasReproducaoFormigas;
    }

    public Integer getCaloriasRemovidasReproducaoFormigas() {
        return caloriasRemovidasReproducaoFormigas;
    }

    public void setCaloriasRemovidasReproducaoFormigas(Integer caloriasRemovidasReproducaoFormigas) {
        this.caloriasRemovidasReproducaoFormigas = caloriasRemovidasReproducaoFormigas;
    }

    public Integer getTempoGeracaoAcucares() {
        return tempoGeracaoAcucares;
    }

    public void setTempoGeracaoAcucares(Integer tempoGeracaoAcucares) {
        this.tempoGeracaoAcucares = tempoGeracaoAcucares;
    }

    public Integer getMovimentosPorDirecao() {
        return movimentosPorDirecao;
    }

    public void setMovimentosPorDirecao(Integer movimentosPorDirecao) {
        this.movimentosPorDirecao = movimentosPorDirecao;
    }

    public Integer getIncrementoPorIngestao() {
        return incrementoPorIngestao;
    }

    public void setIncrementoPorIngestao(Integer incrementoPorIngestao) {
        this.incrementoPorIngestao = incrementoPorIngestao;
    }

    public Integer getIntervaloReproducaoTamandua() {
        return intervaloReproducaoTamandua;
    }

    public void setIntervaloReproducaoTamandua(Integer intervaloReproducaoTamandua) {
        this.intervaloReproducaoTamandua = intervaloReproducaoTamandua;
    }
}
