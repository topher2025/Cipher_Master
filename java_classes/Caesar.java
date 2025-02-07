public class Caesar extends CipherWithIntKey {

    public Caesar(String input, boolean encode, int key) {
        super(input, encode, key);
        FULL = true;
    }

    public String encode() {
        StringBuilder output = new StringBuilder();
        for (char character : plaintext.toCharArray()) {
            if (charInAlphabet(character)) {
                int index = (charIndexInAlphabet(character) + key) % ALPHABET.length();
                output.append(ALPHABET.charAt(index));
            } else {
                output.append(character);
            }
        }
        this.ciphertext = output.toString();
        return this.ciphertext;
    }

    public String decode() {
        StringBuilder output = new StringBuilder();
        for (char character : ciphertext.toCharArray()) {
            if (charInAlphabet(character)) {
                int index = (charIndexInAlphabet(character) - key) % ALPHABET.length();
                output.append(ALPHABET.charAt(index));
            } else{
                output.append(character);
            }
        }
        this.plaintext = output.toString();
        return this.plaintext;
    }
}

