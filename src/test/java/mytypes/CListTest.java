package mytypes;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static mytypes.CList.*;
import static mytypes.CListTest.IntPair.intPair;
import static mytypes.CList.Pair.pair;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class CListTest {

    static class IntPair extends Pair<Integer, Integer> {

        IntPair(Integer _1, Integer _2) {
            super(_1, _2);
        }

        static IntPair intPair(Integer _1, Integer _2) {
            return new IntPair(_1, _2);
        }
    }

    @Test
    public void test01_CListAppendCList_equals_CList() {
        assertThat(clist().append(clist()), is(clist()));
    }

    @Test
    public void test02_leftIdentity_CListAppendCList123_equals_CList123() {
        assertThat(clist().append(clist(1, 2, 3)), is(clist(1, 2, 3)));
        assertThat(clist().append(clist(1, 2, 3)).toList(), is(asList(1, 2, 3)));
    }

    @Test
    public void test03_rightIdentity_CList123MappendCList_equals_CList123() {
        assertThat(clist(1, 2, 3).append(clist()), is(clist(1, 2, 3)));
        assertThat(clist(1, 2, 3).append(clist()).toList(), is(asList(1, 2, 3)));
    }

    @Test
    public void test04_associativity_CList123MappendCList56_MappendCList89_equals_MappendCList123_CList56MappendCList89() {
        assertThat((clist(1, 2, 3).append(clist(5, 6))).append(clist(8, 9)), is(clist(1, 2, 3).append((clist(5, 6).append(clist(8, 9))))));
        assertThat((clist(1, 2, 3).append(clist(5, 6))).append(clist(8, 9)).toList(), is(clist(1, 2, 3).append((clist(5, 6).append(clist(8, 9)))).toList()));
    }

    @Test
    public void test05_concat_should_return_the_CList_of_a_List_of_CList_objects_as_a_CList() {
        CList<CList<Integer>> clists = clist(clist(1, 2, 3), clist(5, 6), clist(8, 9));
        assertThat(clists.concat(), is(clist(1, 2, 3, 5, 6, 8, 9)));
        assertThat(clists.concat().toList(), is(asList(1, 2, 3, 5, 6, 8, 9)));
    }

    // --- Test the other features of CList

    @Test
    public void test06_Empty_CList_toList_should_be_equal_to_empty_List() {
        assertThat(clist().toList(), is(emptyList()));
    }

    @Test
    public void test07_CList_xyz_toList_should_be_equal_to_List_xyz() {
        assertThat(clist(1, 2, 3).toList(), is(asList(1, 2, 3)));
        assertThat(clist('x', 'y', 'z').toList(), is(asList('x', 'y', 'z')));
        assertThat(clist("x", "y", "z").toList(), is(asList("x", "y", "z")));
    }

    @Test
    public void test08_CList_xyz_toSet_should_contain_all_elements_of_the_CList() {
        assertThat(clist(1, 2, 3).toSet().size(), is(3));
        assertThat(clist(1, 2, 3).toSet().contains(1), is(true));
        assertThat(clist(1, 2, 3).toSet().contains(2), is(true));
        assertThat(clist(1, 2, 3).toSet().contains(3), is(true));
        assertThat(clist('x', 'y', 'z').toSet().size(), is(3));
        assertThat(clist('x', 'y', 'z').toSet().contains('x'), is(true));
        assertThat(clist('x', 'y', 'z').toSet().contains('y'), is(true));
        assertThat(clist('x', 'y', 'z').toSet().contains('z'), is(true));
        assertThat(clist("x", "y", "z").toSet().size(), is(3));
        assertThat(clist("x", "y", "z").toSet().contains("x"), is(true));
        assertThat(clist("x", "y", "z").toSet().contains("y"), is(true));
        assertThat(clist("x", "y", "z").toSet().contains("z"), is(true));
    }

    @Test
    public void test09_Cons_should_be_able_to_prepend_an_element() {
        assertThat(cons(15, clist()), is(clist(15)));
        assertThat(cons(15, clist(1, 2, 3)), is(clist(15, 1, 2, 3)));
    }

    @Test
    public void test10_CListHead_should_return_the_first_element() {
        assertThat(clist(1, 2, 3).head(), is(1));
        try {
            clist().head();
            fail("Expected a NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage(), is("head of empty CList"));
        }
    }

    @Test
    public void test11_CListTail_should_return_a_CList_containing_all_elements_except_the_1st_one() {
        assertThat(clist(1, 2, 3).tail(), is(clist(2, 3)));
        try {
            clist().tail();
            fail("Expected a NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            assertThat(e.getMessage(), is("tail of empty CList"));
        }
    }

    @Test
    public void test12_CListHeadOption_should_return_an_Optional_containing_the_first_element_or_an_empty_Optional_if_the_CList_is_empty() {
        assertThat(clist(1, 2, 3).headOption().get(), is(1));
        assertThat(clist(1, 2, 3).headOption().isPresent(), is(true));
        assertThat(clist().headOption().isPresent(), is(false));
    }

    @Test
    public void test13_CListLength_should_return_the_length_of_the_CList() {
        assertThat(clist(1, 2, 3).length(), is(3));
        assertThat(clist().length(), is(0));
    }

    @Test
    public void test14_CListIsDefinedAtIndex_should_return_true_if_an_element_exists_at_the_given_index_otherwise_false() {
        assertThat(clist().isDefinedAt(0), is(false));
        assertThat(clist(1, 2, 3).isDefinedAt(-1), is(false));
        assertThat(clist(1, 2, 3).isDefinedAt(0), is(true));
        assertThat(clist(1, 2, 3).isDefinedAt(1), is(true));
        assertThat(clist(1, 2, 3).isDefinedAt(2), is(true));
        assertThat(clist(1, 2, 3).isDefinedAt(3), is(false));
    }

    @Test
    public void test15_CListContains_should_return_true_if_the_CList_contains_the_given_element_otherwise_false() {
        assertThat(clist().contains("fun"), is(false));
        assertThat(clist("Scala", "is", "fun").contains("Scala"), is(true));
        assertThat(clist("Scala", "is", "fun").contains("is"), is(true));
        assertThat(clist("Scala", "is", "fun").contains("fun"), is(true));
        assertThat(clist("Scala", "is", "fun").contains("Java"), is(false));
    }

    @Test
    public void test16_CListExistsPredicate_should_return_true_if_at_least_one_element_exists_for_which_the_predicate_is_true_otherwise_false() {
        assertThat(clist().exists(e -> ((String) e).length() == 0), is(false));
        assertThat(clist("Scala", "is", "fun").exists(e -> e.length() == 0), is(false));
        assertThat(clist("Scala", "is", "fun").exists(e -> e.length() == 1), is(false));
        assertThat(clist("Scala", "is", "fun").exists(e -> e.length() == 2), is(true));
        assertThat(clist("Scala", "is", "fun").exists(e -> e.length() == 3), is(true));
        assertThat(clist("Scala", "is", "fun").exists(e -> e.length() == 4), is(false));
        assertThat(clist("Scala", "is", "fun").exists(e -> e.length() == 5), is(true));
        assertThat(clist("Scala", "is", "fun").exists(e -> e.length() == 6), is(false));
    }

    @Test
    public void test17_CListForallPredicate_should_return_true_if_the_predicate_is_true_for_all_elements_otherwise_false() {
        assertThat(clist().forall(e -> ((String) e).length() == -27), is(true));
        assertThat(clist("Scala", "is", "fun").forall(e -> e.length() == 0), is(false));
        assertThat(clist("Scala", "is", "fun").forall(e -> e.length() == 1), is(false));
        assertThat(clist("Scala", "is", "fun").forall(e -> e.length() == 2), is(false));
        assertThat(clist("Scala", "is", "fun").forall(e -> e.length() == 3), is(false));
        assertThat(clist("Scala", "is", "fun").forall(e -> e.length() == 4), is(false));
        assertThat(clist("Scala", "is", "fun").forall(e -> e.length() == 5), is(false));
        assertThat(clist("Scala", "is", "fun").forall(e -> e.length() > 1), is(true));
    }

    @Test
    public void test18_CListReverse_should_return_the_CList_with_the_elements_in_reversed_order() {
        assertThat(clist().reverse(), is(clist()));
        assertThat(clist("Scala", "is", "fun").reverse(), is(clist("fun", "is", "Scala")));
        assertThat(clist("Scala", "is", "fun").reverse().reverse(), is(clist("Scala", "is", "fun")));
    }

    @Test
    public void test19_CListIsPalindrome_should_return_true_if_the_original_CList_equals_the_reversed_CList_otherwise_false() {
        assertThat(clist().isPalindrom(), is(true));
        assertThat(clist("live", "is", "live").isPalindrom(), is(true));
        assertThat(clist("live", "is", "love").isPalindrom(), is(false));
    }

    @Test
    public void test20_CListTakeN_should_return_the_first_n_elements_if_n_le_length_else_all_elements() {
        assertThat(clist(1, 2, 3, 4, 5).take(-1), is(clist()));
        assertThat(clist(1, 2, 3, 4, 5).take(0), is(clist()));
        assertThat(clist(1, 2, 3, 4, 5).take(3), is(clist(1, 2, 3)));
        assertThat(clist(1, 2, 3, 4, 5).take(5), is(clist(1, 2, 3, 4, 5)));
        assertThat(clist(1, 2, 3, 4, 5).take(6), is(clist(1, 2, 3, 4, 5)));
    }

    @Test
    public void test21_CListDropN_should_return_a_CList_containing_remaining_elements_after_the_first_n_if_n_le_length_else_no_elements() {
        assertThat(clist(1, 2, 3, 4, 5).drop(-1), is(clist(1, 2, 3, 4, 5)));
        assertThat(clist(1, 2, 3, 4, 5).drop(0), is(clist(1, 2, 3, 4, 5)));
        assertThat(clist(1, 2, 3, 4, 5).drop(3), is(clist(4, 5)));
        assertThat(clist(1, 2, 3, 4, 5).drop(5), is(clist()));
        assertThat(clist(1, 2, 3, 4, 5).drop(6), is(clist()));
    }

    @Test
    public void test22_CListFilterPredicate_should_return_a_CList_containing_all_elements_for_which_the_predicate_is_true() {
        Predicate<Integer> even = x -> x % 2 == 0;
        assertThat(clist(1, 2, 3, 4, 5).filter(even), is(clist(2, 4)));
        assertThat(clist(1, 2, 3, 4, 5).filterNot(even), is(clist(1, 3, 5)));
    }

    @Test
    public void test23_CListMapF_should_return_a_CList_containing_the_results_of_processing_all_elements_with_the_function_f() {
        assertThat(clist(1, 2, 3).map(x -> x + 1), is(clist(2, 3, 4)));
        assertThat(clist(1, 2, 3).map(x -> x * x), is(clist(1, 4, 9)));
        assertThat(clist("Scala", "is", "fun").map(String::toUpperCase), is(clist("SCALA", "IS", "FUN")));
        assertThat(clist("Scala", "is", "fun").map(String::length), is(clist(5, 2, 3)));
        assertThat(clist("Scala", "is", "fun").map(s -> pair(s, s.length())), is(clist(pair("Scala", 5), pair("is", 2), pair("fun", 3))));
    }

    @Test
    public void test24_CListFlatMapF_should_return_a_CList_containing_the_flattend_results_of_processing_all_elements_with_the_function_f() {
        assertThat(clist(1, 2, 3).flatMap(x -> clist(x + 1)), is(clist(2, 3, 4)));
        assertThat(clist(1, 2, 3).flatMap(x -> clist(x * x)), is(clist(1, 4, 9)));
        assertThat(clist("Scala", "is", "fun").flatMap(s -> clist(s.toUpperCase())), is(clist("SCALA", "IS", "FUN")));
        assertThat(clist("Scala", "is", "fun").flatMap(s -> clist(s.length())), is(clist(5, 2, 3)));
        assertThat(clist("Scala", "is", "fun").flatMap(s -> clist(pair(s, s.length()))), is(clist(pair("Scala", 5), pair("is", 2), pair("fun", 3))));
    }

    @Test
    public void test25_CListFlatten_should_flatten_a_CList_of_CLists_but_fail_if_the_CList_contains_elements_other_than_CLists() {
        // flatten() can flatten a CList[CList[String]] into a CList[String]
        assertThat(clist(clist("Scala"), clist("is"), clist("fun")).flatten(), is(clist("Scala", "is", "fun")));
        // flatten() cannot flatten  a CList[String]
        try {
            clist("Scala", "is", "fun").flatten();
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), is("CList cannot be flattened, it is not a CList of CLists."));
        }
    }

    @Test
    public void test26_CListFoldRight_should_return_expected_result_of_right_fold_catamorphism() {
        assertThat(clist().foldRight(0, (elem, acc) -> 1 + acc), is(0));                    // length
        assertThat(clist(1, 2, 3, 4, 5).foldRight(0, (elem, acc) -> 1 + acc), is(5));
        assertThat(CList.<Integer>clist().foldRight(0, (elem, acc) -> elem + acc), is(0));  // sum
        assertThat(clist(1, 2, 3, 4, 5).foldRight(0, (elem, acc) -> elem + acc), is(15));
        assertThat(CList.<Integer>clist().foldRight(1, (elem, acc) -> elem + acc), is(1));  // product
        assertThat(clist(1, 2, 3, 4, 5).foldRight(1, (elem, acc) -> elem * acc), is(120));
        assertThat(CList.<Integer>clist().foldRight(intPair(0, 0), (elem, acc) -> intPair(elem + acc._1, 1 + acc._2)), is(intPair(0, 0)));
        assertThat(clist(1, 2, 3, 4, 5).foldRight(intPair(0, 0), (elem, acc) -> intPair(elem + acc._1, 1 + acc._2)), is(intPair(15, 5)));
    }

    @Test
    public void test27_CListFoldLeft_should_return_expected_result_of_left_fold_catamorphism() {
        assertThat(clist().foldLeft(0, (acc, elem) -> 1 + acc), is(0));                    // length
        assertThat(clist(1, 2, 3, 4, 5).foldLeft(0, (acc, elem) -> 1 + acc), is(5));
        assertThat(CList.<Integer>clist().foldLeft(0, (acc, elem) -> elem + acc), is(0));  // sum
        assertThat(clist(1, 2, 3, 4, 5).foldLeft(0, (acc, elem) -> elem + acc), is(15));
        assertThat(CList.<Integer>clist().foldLeft(1, (acc, elem) -> elem + acc), is(1));  // product
        assertThat(clist(1, 2, 3, 4, 5).foldLeft(1, (acc, elem) -> elem * acc), is(120));
        assertThat(CList.<Integer>clist().foldLeft(intPair(0, 0), (acc, elem) -> intPair(elem + acc._1, 1 + acc._2)), is(intPair(0, 0)));
        assertThat(clist(1, 2, 3, 4, 5).foldLeft(intPair(0, 0), (acc, elem) -> intPair(elem + acc._1, 1 + acc._2)), is(intPair(15, 5)));
    }

    @Test
    public void test28_CListFold_should_return_expected_result_of_the_binary_fold_operator() {
        assertThat(CList.<Integer>clist().fold(0, (elem, acc) -> 1 + acc), is(0));          // length
        assertThat(clist(1, 2, 3, 4, 5).fold(0, (elem, acc) -> 1 + acc), is(5));
        assertThat(CList.<Integer>clist().fold(0, (elem, acc) -> elem + acc), is(0));       // sum
        assertThat(clist(1, 2, 3, 4, 5).fold(0, (elem, acc) -> elem + acc), is(15));
        assertThat(CList.<Integer>clist().fold(1, (elem, acc) -> elem + acc), is(1));       // product
        assertThat(clist(1, 2, 3, 4, 5).fold(1, (elem, acc) -> elem * acc), is(120));
    }

    @Test
    public void test29_CListAppend_or_CListMappend_should_concatenate_two_CLists_into_one() {
        // append: takes a CList argument
        // mappend: takes a Monoid argument (CList is a Monoid)
        assertThat(clist("Scala", "is").append(clist("great", "fun")), is(clist("Scala", "is", "great", "fun")));
        assertThat(clist(1, 2, 3).append(clist(4, 5)), is(clist(1, 2, 3, 4, 5)));
    }

    @Test
    public void test30_CListConcat_or_CListMconcat_should_concatenate_a_List_of_CLists_into_one_CList() {
        // concat (static method): takes a Lists of CLists
        // mconcat (instance method): takes a List of Monoids (CList is a Monoid)
        CList<CList<Integer>> clistOfCListsInteger = clist(clist(1, 2, 3), clist(4, 5), clist(11, 12, 13));
        assertThat(clistOfCListsInteger.concat(), is(clist(1, 2, 3, 4, 5, 11, 12, 13)));
        CList<CList<String>> clistOfCListsString = clist(clist("Scala", "is"), clist("great", "fun"));
        assertThat(clistOfCListsString.concat(), is(clist("Scala", "is", "great", "fun")));
    }

    @Test
    public void test31_CListZipWithCListF_should_process_corresponding_elements_in_2_CList_and_return_a_CList_of_results() {
        BiFunction<Integer, String, Pair<Integer, String>> mkTuple = Pair::pair;
        assertThat(clist(1, 2, 3).zipWith(mkTuple, clist("Scala", "is", "fun")), is(clist(pair(1, "Scala"), pair(2, "is"), pair(3, "fun"))));
        assertThat(clist(1, 2, 3, 4).zipWith(mkTuple, clist("Scala", "is", "fun")), is(clist(pair(1, "Scala"), pair(2, "is"), pair(3, "fun"))));
        assertThat(clist(1, 2, 3).zipWith(mkTuple, clist("Scala", "is", "great", "fun")), is(clist(pair(1, "Scala"), pair(2, "is"), pair(3, "great"))));
        assertThat(clist(1, 2, 3, 4).zipWith(mkTuple, clist()), is(clist()));
        assertThat(CList.<Integer>clist().zipWith(mkTuple, clist("Scala", "is", "great", "fun")), is(clist()));
        assertThat(clist(1, 2, 3, 4).zipWith((x, y) -> x + y, clist(1, 2, 3, 4)), is(clist(2, 4, 6, 8)));
        assertThat(clist(1, 2, 3, 4).zipWith((x, y) -> x * y, clist(1, 2, 3, 4)), is(clist(1, 4, 9, 16)));
    }

    @Test
    public void test32_CListZipCList_should_return_a_CList_of_pairs_with_the_elements_of_both_CLists() {
        BiFunction<Integer, String, Pair<Integer, String>> mkTuple = Pair::pair;
        assertThat(clist(1, 2, 3).zip(clist("Scala", "is", "fun")), is(clist(pair(1, "Scala"), pair(2, "is"), pair(3, "fun"))));
        assertThat(clist(1, 2, 3, 4).zip(clist("Scala", "is", "fun")), is(clist(pair(1, "Scala"), pair(2, "is"), pair(3, "fun"))));
        assertThat(clist(1, 2, 3).zip(clist("Scala", "is", "great", "fun")), is(clist(pair(1, "Scala"), pair(2, "is"), pair(3, "great"))));
        assertThat(clist(1, 2, 3, 4).zip(clist()), is(clist()));
        assertThat(CList.<Integer>clist().zip(clist("Scala", "is", "great", "fun")), is(clist()));
    }

    @Test
    public void test33_CListZipCList_should_return_a_CList_of_pairs_with_the_elements_and_thre_indices_of_the_CList() {
        assertThat(clist().zipWithIndex(), is(clist()));
        assertThat(clist(1, 2, 3).zipWithIndex(), is(clist(pair(1, 0), pair(2, 1), pair(3, 2))));
        assertThat(clist("Scala", "is", "fun").zipWithIndex(), is(clist(pair("Scala", 0), pair("is", 1), pair("fun", 2))));
    }
}
