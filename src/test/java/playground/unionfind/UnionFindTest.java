package playground.unionfind;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnionFindTest {

    @Test
    public void test1() {
        UnionFindLeetCode uf = new UnionFindLeetCode(10);
        // 1-2-5-6-7 3-8-9 4
        uf.union(1, 2);
        uf.union(2, 5);
        uf.union(5, 6);
        uf.union(6, 7);
        uf.union(3, 8);
        uf.union(8, 9);

        uf.union(9, 4);
        // 1-2-5-6-7 3-8-9-4
        int i = uf.find(7);
        Assertions.assertThat(i).isEqualTo(1);
        Assertions.assertThat(i).isEqualTo(uf.findWithPathCompression(7));
    }

}
