package util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Classe util para limitar a quantidade de caracteres
 * digitados em campos de texto do Swing.
 * Ajuda a controlar a entrada e evitar excesso de digitaçao.
 */
public class LimiteCaracteres extends PlainDocument {

    /** Limite maximo de caracteres permitido. */
    private int limite;

    /**
     * Cria o limitador definindo quantos caracteres sao aceitos.
     *
     * @param limite numero maximo permitido
     */
    public LimiteCaracteres(int limite) {
        super();
        this.limite = limite;
    }

    /**
     * Sobrescreve o metodo de inserçao para impedir que o usuario ultrapasse o limite.
     *
     * @param offset posicao da insercao
     * @param str texto a inserir
     * @param attr atributos
     * @throws BadLocationException se a posicao for invalida
     */
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if (getLength() + str.length() <= limite) {
            super.insertString(offset, str, attr);
        }
    }
}
