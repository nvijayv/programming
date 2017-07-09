import java.io.*;
import java.util.*;
import java.net.URISyntaxException;

class Trie {
    public static int count = 1;
    private static final char BEGIN_CHAR = '^';
    private static final char[] specialChars = {'\'', '-', '/'};
    private static TrieNode root = new TrieNode(BEGIN_CHAR);

    private static String formatString(final String str) {
        return BEGIN_CHAR + str;
    }

    public static void addString(final String str) {
        addString(root, formatString(str));
    }

    private static void addString(final TrieNode node, final String str) {
        if (str.isEmpty()) return;

        int length = str.length();
        if (length == 1) {
            node.isWord = true;
        } else {
            String restStr = str.substring(1);
            char nextChar = restStr.charAt(0);
            if (!node.children.containsKey(nextChar)) {
                TrieNode child = new TrieNode(nextChar);
                child.parent = node;
                node.children.put(nextChar, child);
                ++count;
            }
            addString(node.children.get(nextChar), restStr);
        }
    }

    /* Returns a sequence of all the non-empty prefixes of 'str' which match in the Trie, ordered by length */
    public static LinkedList<String> searchString(final String str) {
        LinkedList<String> results = new LinkedList<String>();
        searchString(root, formatString(str), 1, results);
        LinkedList<String> newResult = new LinkedList<String>();
        for (String rstr : results) {
            newResult.add(rstr.substring(1));
        }
        return newResult;
    }

    private static void searchString(final TrieNode node, final String str, final int index, LinkedList<String> results) {
        if (node != null && str.length() >= index && str.charAt(index-1) == node.getChar()) {
            if (node.isWord) {
                results.add(str.substring(0, index));
            }
            if (str.length() >= index+1) {
                final char nextChar = str.charAt(index);
                if (node.children.containsKey(nextChar)) {
                    searchString(node.children.get(nextChar), str, index + 1, results);
                }
            }
        }
    }

    private static boolean isSpecialChar(char c) {
        for (char ch: specialChars) {
            if (c == ch) { return true; }
        }
        return false;
    }

    private static CharType getCharType(char c) {
        if (Character.isAlphabetic(c) || isSpecialChar(c)) { return CharType.CHAR; }
        else { return CharType.NUM; }
    }

    private static LinkedList<String> partition(String str) {
        LinkedList<String> strings = new LinkedList<String>();
        int length = str.length();
        int beginIndex = 0;
        int endIndex = 0;
        CharType ctype;

        while (endIndex < length) {
            ctype = getCharType(str.charAt(endIndex));
            do {
                endIndex++;
            } while (endIndex < length && ctype == getCharType(str.charAt(endIndex)));
            strings.add(str.substring(beginIndex, endIndex));
            beginIndex = endIndex;
        }
        return strings;
    }

    private static boolean getWords(String str, LinkedList<String> words) {
        LinkedList<String> prefixWords = searchString(str);
        if (!prefixWords.isEmpty()) {
            int size = prefixWords.size();
            for (int i = size-1; i >= 0; i--) {
                final String prefix = prefixWords.get(i);
                words.add(prefix);
                if (prefix.equals(str)) {
                    return true;
                } else {
                    final String suffix = str.substring(prefix.length(), str.length());
                    final boolean foundWords = getWords(suffix, words);
                    if (foundWords) { return true; }
                    else {
                        words.removeLast();
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private static LinkedList<String> tokenise(String str) {
        LinkedList<String> tokens = new LinkedList<String>();
        LinkedList<String> strings = partition(str);

        for (String string : strings) {
            if (getCharType(string.charAt(0)) == CharType.NUM) {
                tokens.add(string);
            } else {
                LinkedList<String> words = new LinkedList<String>();
                final boolean foundWords = getWords(string, words);
                if (foundWords) {
                    for (String word : words) {
                        tokens.add(word);
                    }
                } else {
                    tokens.clear();
                    tokens.add(str);
                    break;
                }
            }
        }
        return tokens;
    }

    private static String cleanup(String str) throws URISyntaxException{
        LinkedList<String> tokens;
        String result = "";

        if (str.startsWith("#")) {
            str = str.substring(1);
        }
        if (str.startsWith("www.")) {
            str = str.substring(4);
        }

        int ind = str.indexOf('.');
        if (ind != -1) {
            str = str.substring(0, ind);
        }

        tokens = tokenise(str);
        for (String token : tokens) {
            result += " " + token;
        }
        return result.trim();
    }

    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("words.txt"));
            while (in.ready()) {
                String word = in.readLine().trim();
                Trie.addString(word.toLowerCase());
            }
            in.close();

            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            int numStrings = Integer.parseInt(stdin.readLine().trim());
            for (int i=0; i<numStrings; i++) {
                String str = stdin.readLine().trim();
                System.out.println(cleanup(str.toLowerCase()));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

class TrieNode {
    private char c;
    public TrieNode parent = null;
    public boolean isWord = false;
    public HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>(26);

    public TrieNode(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }
}

enum CharType {
    CHAR, NUM
}