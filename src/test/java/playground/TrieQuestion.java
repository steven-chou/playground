package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TrieQuestion {
    /**
     * Implement Trie (Prefix Tree)
     * A trie (pronounced as "try") or prefix tree is a tree data structure used to efficiently store and retrieve keys in a
     * dataset of strings.
     * There are various applications of this data structure, such as autocomplete and spellchecker.
     * <p>
     * Implement the Trie class:
     * <p>
     * Trie() Initializes the trie object.
     * void insert(String word) Inserts the string word into the trie.
     * boolean search(String word) Returns true if the string word is in the trie (i.e., was inserted before), and false otherwise.
     * boolean startsWith(String prefix) Returns true if there is a previously inserted string word that has the prefix, and false otherwise.
     * <p>
     * Input
     * ["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
     * [[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
     * Output
     * [null, null, true, false, true, null, true]
     * <p>
     * Explanation
     * Trie trie = new Trie();
     * trie.insert("apple");
     * trie.search("apple");   // return True
     * trie.search("app");     // return False
     * trie.startsWith("app"); // return True
     * trie.insert("app");
     * trie.search("app");     // return True
     * <p>
     * https://leetcode.com/problems/implement-trie-prefix-tree/editorial/
     * <p>
     * Time complexity
     * If the longest length of the word is N, the height of Trie will be N + 1. Therefore, the time complexity of all
     * insert, search and startsWith methods will be O(N).
     * <p>
     * Space complexity
     * If we have M words to insert in total and the length of words is at most N, there will be at most M*N nodes in
     * the worst case (any two words don't have a common prefix).
     * Let's assume that there are maximum K different characters (K is equal to 26 in this problem, but might differ in
     * different cases). So each node will maintain a map whose size is at most K.
     * Therefore, the space complexity will be O(M*N*K).
     * It seems that Trie is really space consuming, however, the real space complexity of Trie is much smaller than our
     * estimation, especially when the distribution of words is dense.
     * Trie can also be implemented by the array(TrieArray class below) which will achieve a slightly better time performance
     * but a slightly lower space performance.
     */
    class TrieMap {
        class TrieNode {
            private boolean isWord;
            private final Map<Character, TrieNode> charToChild = new HashMap<>();
        }

        private final TrieNode root;

        public TrieMap() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode currentNode = root;
            for (int i = 0; i < word.length(); i++) {
                currentNode = currentNode.charToChild.computeIfAbsent(word.charAt(i), k -> new TrieNode());
            }
            currentNode.isWord = true;
        }

        public boolean search(String word) {
            TrieNode currentNode = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (currentNode.charToChild.containsKey(c)) {
                    currentNode = currentNode.charToChild.get(c);
                } else
                    return false;
            }
            return currentNode.isWord;
        }

        public boolean startsWith(String prefix) {
            TrieNode currentNode = root;
            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);
                if (currentNode.charToChild.containsKey(c)) {
                    currentNode = currentNode.charToChild.get(c);
                } else
                    return false;
            }
            return true;
        }
    }

    class TrieArray {
        private class TrieNode {
            private boolean isWord;
            private final TrieArray.TrieNode[] children;

            public TrieNode() {
                children = new TrieNode[26]; // a-z
            }

            boolean containsKey(char c) {
                return children[c - 'a'] != null;
            }

            void put(char c, TrieNode node) {
                children[c - 'a'] = node;
            }

            TrieNode get(char c) {
                return children[c - 'a'];
            }
        }

        private final TrieNode root;

        public TrieArray() {
            root = new TrieNode();
        }

        public void insert(String word) {
            TrieNode currentNode = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (!currentNode.containsKey(c)) {
                    currentNode.put(c, new TrieNode());
                }
                currentNode = currentNode.get(c);
            }
            currentNode.isWord = true;
        }

        public boolean search(String word) {
            TrieNode currentNode = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (currentNode.containsKey(c)) {
                    currentNode = currentNode.get(c);
                } else
                    return false;
            }
            return currentNode.isWord;
        }

        public boolean startsWith(String prefix) {
            TrieNode currentNode = root;
            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);
                if (currentNode.containsKey(c)) {
                    currentNode = currentNode.get(c);
                } else
                    return false;
            }
            return true;
        }
    }

    @Test
    void testTrie() {
        TrieMap trieMap = new TrieMap();
        trieMap.insert("apple");
        Assertions.assertThat(trieMap.search("apple")).isTrue();
        Assertions.assertThat(trieMap.search("app")).isFalse();
        Assertions.assertThat(trieMap.startsWith("app")).isTrue();
        trieMap.insert("app");
        Assertions.assertThat(trieMap.search("app")).isTrue();
        TrieArray trieArray = new TrieArray();
        trieArray.insert("apple");
        Assertions.assertThat(trieArray.search("apple")).isTrue();
        Assertions.assertThat(trieArray.search("app")).isFalse();
        Assertions.assertThat(trieArray.startsWith("app")).isTrue();
        trieArray.insert("app");
        Assertions.assertThat(trieArray.search("app")).isTrue();
    }

    /**
     * Search Suggestions System
     * You are given an array of strings products and a string searchWord.
     * <p>
     * Design a system that suggests at most three product names from products after each character of
     * searchWord is typed. Suggested products should have common prefix with searchWord. If there are more
     * than three products with a common prefix return the three lexicographically minimums products.
     * <p>
     * Return a list of lists of the suggested products after each character of searchWord is typed.
     * <p>
     * <p>
     * Input: products = ["mobile","mouse","moneypot","monitor","mousepad"], searchWord = "mouse"
     * Output: [["mobile","moneypot","monitor"],["mobile","moneypot","monitor"],["mouse","mousepad"],
     * ["mouse","mousepad"],["mouse","mousepad"]]
     * Explanation: products sorted lexicographically = ["mobile","moneypot","monitor","mouse","mousepad"].
     * After typing m and mo all products match and we show user ["mobile","moneypot","monitor"].
     * After typing mou, mous and mouse the system suggests ["mouse","mousepad"].
     * <p>
     * Input: products = ["havana"], searchWord = "havana"
     * Output: [["havana"],["havana"],["havana"],["havana"],["havana"],["havana"]]
     * Explanation: The only word "havana" will be always suggested while typing the search word.
     * <p>
     * Lexicographical order:
     * It means "dictionary order", i.e., the way in which words are ordered in a dictionary. If you were
     * to determine which one of the two words would come before the other in a dictionary, you would compare
     * the words letter by the letter starting from the first position. For example, the word "children" will
     * appear before (and can be considered smaller) than the word "chill" because the first four letters
     * of the two words are the same but the letter at the fifth position in "children" (i.e. d ) comes before
     * (or is smaller than) the letter at the fifth position in "chill" (i.e. l ). Observe that lengthwise,
     * the word "children" is bigger than "chill" but length is not the criteria here. For the same reason,
     * an array containing 12345 will appear before an array containing 1235.
     * <p>
     * https://leetcode.com/problems/search-suggestions-system/description/
     */
    @Test
    void testSuggestedProducts() {
        String[] products = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
        Assertions.assertThat(suggestedProducts(products, "mouse")).
                containsExactly(List.of("mobile", "moneypot", "monitor"), List.of("mobile", "moneypot", "monitor"), List.of("mouse", "mousepad"), List.of("mouse", "mousepad"), List.of("mouse", "mousepad"));
    }

    /**
     * Build a trie and insert all products, then for each search strings, find the prefix node in trie then perform
     * DFS preorder traversal over its children until reaching the word node.
     * <p>
     * Algo:
     * 1. Create a Trie from the given products input.
     * 2. Iterate each char of the searchWord and append it to the searchWordPrefix StringBuilder to search for each time.
     * 3. Traverse the trie to the node representing the searchWordPrefix.
     * 4. Now traverse the tree from current node in a DFS preorder fashion and record whenever we visit a word node.
     * 5. Limit the result to 3 and return dfs once reached this limit.
     * 6. Add the words to the final result.
     * <p>
     * Time complexity : O(M) to build the trie, where M is total number of characters in products For each
     * prefix we find its representative node in O(len(prefix)) and dfs to find at most 3 words which is an
     * O(1) operation. Thus the overall complexity is dominated by the time required to build the trie.
     * <p>
     * Space complexity : O(26n)=O(n). Here n is the number of nodes in the trie. 26 is the alphabet size.
     * Space required for output is O(m) where m is the length of the search word.
     * <p>
     * Other solution includes
     * 1. Binary search for the prefix. Once we locate the first match of prefix, all we need to do is to add the next
     * 3 words into the result (if there are any), since we sorted the words beforehand. (LeetCode solution)
     * 2. Use two pointers to search on the sorted list (Neetcode)
     */
    private static class ProductTrie {
        static class TrieNode {
            TrieNode[] children = new TrieNode[26];
            boolean isWord;
        }

        TrieNode root = new TrieNode();

        void insert(String word) {
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (current.children[c - 'a'] == null)
                    current.children[c - 'a'] = new TrieNode();
                current = current.children[c - 'a'];
            }
            current.isWord = true;
        }

        // Runs a DFS on trie starting with given prefix and adds all the words to products list, limiting list size to max
        void findProductWithPrefix(TrieNode node, StringBuilder prefixStb, List<String> products, int max) {
            if (products.size() == max)
                return;
            if (node.isWord)
                products.add(prefixStb.toString()); // The "word" node is not necessarily the leaf node, so need to keep going down
            // DFS traversal over all children
            // preorder traversal of a trie will always result in a sorted traversal of results
            for (int i = 0; i < node.children.length; i++) {
                if (node.children[i] != null) {
                    prefixStb.append((char) (i + 'a')); // Use stb to build up the product name from each node we visit
                    findProductWithPrefix(node.children[i], prefixStb, products, max);
                    prefixStb.deleteCharAt(prefixStb.length() - 1); // Remove the last char once we backtrack
                }
            }
        }

        List<String> searchWithPrefix(String prefix, int max) {
            TrieNode current = root;
            // First move down to the prefix node if possible
            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);
                if (current.children[c - 'a'] == null)
                    return new ArrayList<>(); // Couldn't find the prefix
                current = current.children[c - 'a'];
            }
            // Reached the prefix node
            List<String> products = new ArrayList<>();
            findProductWithPrefix(current, new StringBuilder(prefix), products, max);
            return products;
        }
    }

    // The entry point of the solution to the problem
    List<List<String>> suggestedProducts(String[] products, String searchWord) {
        // Build the trie first
        ProductTrie trie = new ProductTrie();
        for (String product : products)
            trie.insert(product);
        List<List<String>> results = new ArrayList<>();
        StringBuilder searchWordPrefix = new StringBuilder();
        for (int i = 0; i < searchWord.length(); i++) {
            searchWordPrefix.append(searchWord.charAt(i));
            results.add(trie.searchWithPrefix(searchWordPrefix.toString(), 3));
        }
        return results;
    }

    /**
     * Design Add and Search Words Data Structure
     * Design a data structure that supports adding new words and finding if a string matches
     * any previously added string.
     * <p>
     * Implement the WordDictionary class:
     * <p>
     * WordDictionary() Initializes the object.
     * void addWord(word) Adds word to the data structure, it can be matched later.
     * bool search(word) Returns true if there is any string in the data structure that matches
     * word or false otherwise. word may contain dots '.' where dots can be matched with any letter.
     * <p>
     * Input
     * ["WordDictionary","addWord","addWord","addWord","search","search","search","search"]
     * [[],["bad"],["dad"],["mad"],["pad"],["bad"],[".ad"],["b.."]]
     * Output
     * [null,null,null,null,false,true,true,true]
     * <p>
     * Explanation
     * WordDictionary wordDictionary = new WordDictionary();
     * wordDictionary.addWord("bad");
     * wordDictionary.addWord("dad");
     * wordDictionary.addWord("mad");
     * wordDictionary.search("pad"); // return False
     * wordDictionary.search("bad"); // return True
     * wordDictionary.search(".ad"); // return True
     * wordDictionary.search("b.."); // return True
     * https://leetcode.com/problems/design-add-and-search-words-data-structure/description/
     * <p>
     * Time complexity: O(M) for the "well-defined" words without dots,
     * where M is the key length, and N is a number of keys,
     * and O(N⋅26^M) for the "undefined" words.
     * <p>
     * Space complexity: O(1) for the search of "well-defined" words without dots, and up to O(M)
     * for the "undefined" words, to keep the recursion stack.
     */
    class WordDictionary {
        private class TrieNode {
            private TrieNode[] children = new TrieNode[26]; // Alternative is HashMap, check LeetCode official answer
            private boolean word;
        }

        TrieNode root;

        public WordDictionary() {
            root = new TrieNode();
        }

        public void addWord(String word) {
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (current.children[c - 'a'] == null)
                    current.children[c - 'a'] = new TrieNode();
                current = current.children[c - 'a'];
            }
            current.word = true;
        }

        private boolean searchFromNode(TrieNode node, String word) {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (c == '.') {
                    // When encountering '.', we need to explore all possible/non-null children for the reaming
                    // substring, so make recursive call with child node (cuz we still need to consume the current node
                    // for '.') and the remaining substring
                    for (int j = 0; j < node.children.length; j++) {
                        if (node.children[j] != null && searchFromNode(node.children[j], word.substring(i + 1)))
                            return true;
                    }
                    return false; // No word node found from all children of this '.' node
                } else {
                    // Normal trie node traversal, move to the next prefix node if any, otherwise return false
                    if (node.children[c - 'a'] == null)
                        return false;
                    node = node.children[c - 'a'];
                }
            }
            return node.word;
        }

        public boolean search(String word) {
            return searchFromNode(root, word);
        }
    }

    @Test
    void testWordDictionary() {
        WordDictionary wordDictionary = new WordDictionary();
        wordDictionary.addWord("bad");
        wordDictionary.addWord("dad");
        wordDictionary.addWord("mad");
        Assertions.assertThat(wordDictionary.search("pad")).isFalse();
        Assertions.assertThat(wordDictionary.search(".ad")).isTrue();
        Assertions.assertThat(wordDictionary.search("b..")).isTrue();
    }

    /**
     * Design Search Autocomplete System
     * Design a search autocomplete system for a search engine. Users may input a sentence
     * (at least one word and end with a special character '#').
     * <p>
     * You are given a string array sentences and an integer array times both of length n
     * where sentences[i] is a previously typed sentence and times[i] is the corresponding
     * number of times the sentence was typed. For each input character except '#', return
     * the top 3 historical hot sentences that have the same prefix as the part of the
     * sentence already typed.
     * <p>
     * Here are the specific rules:
     * <p>
     * The hot degree for a sentence is defined as the number of times a user typed the
     * exactly same sentence before.
     * The returned top 3 hot sentences should be sorted by hot degree (The first is the
     * hottest one). If several sentences have the same hot degree, use ASCII-code order
     * (smaller one appears first).
     * If less than 3 hot sentences exist, return as many as you can.
     * When the input is a special character, it means the sentence ends, and in this case,
     * you need to return an empty list.
     * <p>
     * Implement the AutocompleteSystem class:
     * <p>
     * AutocompleteSystem(String[] sentences, int[] times) Initializes the object with the
     * sentences and times arrays.
     * <p>
     * List<String> input(char c) This indicates that the user typed the character c.
     * Returns an empty array [] if c == '#' and stores the inputted sentence in the system.
     * Returns the top 3 historical hot sentences that have the same prefix as the part of the
     * sentence already typed. If there are fewer than 3 matches, return them all.
     * <p>
     * Input
     * ["AutocompleteSystem", "input", "input", "input", "input"]
     * [[["i love you", "island", "iroman", "i love leetcode"], [5, 3, 2, 2]], ["i"], [" "],
     * ["a"], ["#"]]
     * <p>
     * Output
     * [null, ["i love you", "island", "i love leetcode"], ["i love you", "i love leetcode"],
     * [], []]
     * <p>
     * Explanation
     * AutocompleteSystem obj = new AutocompleteSystem(["i love you", "island", "iroman",
     * "i love leetcode"], [5, 3, 2, 2]);
     * <p>
     * obj.input("i");
     * // return ["i love you", "island", "i love leetcode"]. There are four sentences that
     * have prefix "i". Among them, "ironman" and "i love leetcode" have same hot degree.
     * Since ' ' has ASCII code 32 and 'r' has ASCII code 114, "i love leetcode" should be
     * in front of "ironman". Also we only need to output top 3 hot sentences, so "ironman"
     * will be ignored.
     * <p>
     * obj.input(" ");
     * // return ["i love you", "i love leetcode"]. There are only two sentences that have
     * prefix "i ".
     * <p>
     * obj.input("a"); // return []. There are no sentences that have prefix "i a".
     * <p>
     * obj.input("#");
     * // return []. The user finished the input, the sentence "i a" should be saved as
     * a historical sentence in system. And the following input will be counted as a new search.
     */
    @Test
    void testAutocompleteSystem() {
        AutocompleteSystem obj = new AutocompleteSystem(new String[]{"i love you", "island", "iroman", "i love leetcode"}, new int[]{5, 3, 2, 2});
        // There are four sentences that have prefix "i". Among them, "ironman" and "i love leetcode" have same hot degree. Since ' ' has ASCII code 32
        // and 'r' has ASCII code 114, "i love leetcode" should be in front of "ironman". Also we only need to output top 3 hot sentences, so "ironman" will be ignored.
        Assertions.assertThat(obj.input('i')).containsExactly("i love you", "island", "i love leetcode");
        Assertions.assertThat(obj.input(' ')).containsExactly("i love you", "i love leetcode");
        // There are no sentences that have prefix "i a".
        Assertions.assertThat(obj.input('a')).hasSize(0);
        // The user finished the input, the sentence "i a" should be saved as a historical sentence in system. And the following input will be counted as a new search.
        Assertions.assertThat(obj.input('#')).hasSize(0);
    }

    /**
     *
     */
    class AutocompleteSystem {
        private final TrieNode root;
        private final TrieNode deadNode;
        private TrieNode currentNode;
        private final StringBuilder currentSentence;

        class TrieNode {
            Map<Character, TrieNode> charToChildNode;
            Map<String, Integer> sentenceToCount;

            public TrieNode() {
                this.charToChildNode = new HashMap<>();
                this.sentenceToCount = new HashMap<>();
            }
        }

        /**
         * Initializes the object with the sentences and times arrays.
         * Time complexity: O(n⋅k), n as the length of sentences, k as the average length of all sentences,
         */
        public AutocompleteSystem(String[] sentences, int[] times) {
            root = new TrieNode();
            for (int i = 0; i < sentences.length; i++) {
                addSentenceToTrie(sentences[i], times[i]);
            }
            this.deadNode = new TrieNode();
            currentSentence = new StringBuilder();
            currentNode = root;
        }

        private void addSentenceToTrie(String sentence, int degree) {
            TrieNode current = root;
            for (char c : sentence.toCharArray()) {
                current = current.charToChildNode.computeIfAbsent(c, k -> new TrieNode());
                current.sentenceToCount.put(sentence, current.sentenceToCount.getOrDefault(sentence, 0) + degree);
            }
        }

        /**
         * This indicates that the user typed the character c.
         * Returns an empty array [] if c == '#' and stores the inputted sentence in the system.
         * Returns the top 3 historical hot sentences that have the same prefix as the part of the
         * sentence already typed. If there are fewer than 3 matches, return them all.
         * Time complexity: O(n⋅log n), n as the length of sentences, primarily for the sorting
         * O(n⋅log k), k = 3, when using minHeap
         */
        public List<String> input(char c) {
            if (c == '#') {
                addSentenceToTrie(currentSentence.toString(), 1);
                currentSentence.setLength(0); // reset stb
                currentNode = root;
                return new ArrayList<>();
            }
            // c != #
            if (!currentNode.charToChildNode.containsKey(c)) {
                // no existing sentences that have the current sentence we are typing as a prefix.
                currentNode = deadNode;
                // add it to the current sentence, so it will be saved in the trie later
                currentSentence.append(c);
                return new ArrayList<>();
            } else {
                // There is a child node of the current c at the current node.
                // Add current char to the current sentence, and move current node to this child node
                currentSentence.append(c);
                currentNode = currentNode.charToChildNode.get(c);
                // Now returns the top 3 sentences from the sentenceToDegree map of this node

                // Slightly better solution using minHeap to get Top K sentences.
                // Cuz we use minHeap, the comparator is defined in the opposite way. Sort by degree ascending, then by sentence descending
//                Queue<String> minHeap = new PriorityQueue<>(Comparator.<String>comparingInt(s -> currentNode.sentenceToDegree.get(s)).thenComparing(Comparator.reverseOrder()));
//                for (String s : currentNode.sentenceToDegree.keySet()) {
//                    minHeap.offer(s);
//                    if (minHeap.size() > 3)
//                        minHeap.poll();
//                }
//                List<String> ans = new ArrayList<>();
//                while (!minHeap.isEmpty())
//                    ans.add(minHeap.poll());
//                Collections.reverse(ans);
//                return ans;

                // Use stream API to sort the sentences by degree descending, then by sentence ascending. Then take top 3
                List<String> sentences = new ArrayList<>(currentNode.sentenceToCount.keySet());
                return sentences.stream()
                        .sorted(Comparator.<String>comparingInt(s -> currentNode.sentenceToCount.get(s)).reversed().thenComparing(Comparator.naturalOrder()))
                        .limit(3).collect(Collectors.toList());
            }
        }
    }
}


