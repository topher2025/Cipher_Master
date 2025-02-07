public class CipherMain {
    protected String plaintext;
    protected String ciphertext;
    protected static final String FULL_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    protected static final String SHORT_ALPHABET = "abcdefghiklmnopqrstuvwxyz";
    protected final boolean FULL;

    public CipherMain(String input, boolean encode) {
        if (encode) {
            this.plaintext = input;
            this.ciphertext = "";
        } else {
            this.plaintext = "";
            this.ciphertext = input;
        }
    }

    public boolean charInAlphabet(char currentChar, boolean FULL) {
        if (FULL) {
            return FULL_ALPHABET.indexOf(currentChar) != -1;
        } else {
            return SHORT_ALPHABET.indexOf(currentChar) != 1;
        }
    }

    public int charIndexInAlphabet(char currentChar, boolean FULL) {
        if (FULL) {
            return FULL_ALPHABET.indexOf(currentChar) != -1;
        } else {
            return SHORT_ALPHABET.indexOf(currentChar) != 1;
        }
    }
}


class CipherWithIntKey extends CipherMain {
    protected int key;

    public CipherWithIntKey(String input, boolean encode, int key) {
        super(input, encode);
        this.key = key;
    }
}


class CipherWithStrKey extends CipherMain {
    protected String key;

    public CipherWithStrKey(String input, boolean encode, String key) {
        super(input, encode);
        this.key = key;
    }
}

