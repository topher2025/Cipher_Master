public class Cipher
{
    public void Cipher(String input, boolean encode)
    {
        if encode == true
        {
            public String plaintext = input;
            public String ciphertext;
        }
        else
        {
            public String plaintext;
            public String ciphertext = input;
        }
        private String alphabet = "abcdefghijklmnopqrstuvwxyz";
        public String[] baseAlphabet = new String[25];
        for (int i = 0, i < baseAlphabet.length, i++)
        {
            baseAlphabet[i] = alphabet[i];
        }
    }

    public boolean charInAlphabet(char currentChar)
    {
        private inAlphabet = false;
        for (int i = 0, 1 < baseAlphabet.length, i++)
        {
            if baseAlphabet[i] == currentChar
            {
            inAlphabet = true;
            }
        }
        if inAlphabet == true
        {
            return true;
        }
        else if inAlphabet == false
        {
            return false;
        }
    }

    public int charIndexInAlphabet(char currentChar)
    {
        for (int i = 0, 1 < baseAlphabet.length, i++)
        {
            if baseAlphabet[i] == currentChar
            {
                public characterIndex = i;
            }
        }
        return characterIndex
    }
}


public class CipherWithStrKey extends Cipher
{
    public void CipherWithKey(String input, boolean encode, String strKey)
    {
    Super(input, encode);
    public Sting key = strKey;
    }
}


public class CipherWithIntKey extends Cipher
{
    public void CipherWithKey(String input, boolean encode, int intKey)
    {
    Super(input, encode);
    public int key = intKey;
    }
}


public class Caesar extends CipherWithIntKey
{
    //Constructor
    /**Thiṡ function is the only constructor. It goes up one level and has the same output values as the CipherWithIntKey has.*/
    public void Caesar(String input, boolean encode, int intKey)
    {
        Super(input, encode, intKey);
    }


    public String Encode(String input, int intKey)
    {
        private String character
        private int index
        public String output = "";
        for (int i = 0, i < input.length(), i++)
        {
            char = input[i]
            if charInAlphabet(character) == true
            {
                index = charIndexInAlphabet(character)
                if index > baseAlphabet.length-1
                {
                    index = (-1) * ()baseAlphabet.length - index);
                }
                    output = output + baseAlphabet[index];
            }
            else
            {
                output = output + character;
            }
        ciphertext = output;
        return output;
        }
    }
}
