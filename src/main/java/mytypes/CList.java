package mytypes;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static mytypes.CList.Pair.pair;

/**
 * CList is a ConsList.
 */
public abstract class CList<A> {

    @SuppressWarnings("WeakerAccess")
    public static class Pair<T, U> {

        public final T _1;
        public final U _2;

        public Pair(T _1, U _2) {
            this._1 = _1;
            this._2 = _2;
        }

        public static <A, B> Pair<A, B> pair(A _1, B _2) {
            return new Pair(_1, _2);
        }

        @Override
        public boolean equals(Object o) {

            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Pair<?, ?> pair = (Pair<?, ?>) o;

            return _1.equals(pair._1) && _2.equals(pair._2);
        }

        @Override
        public int hashCode() {
            int result = _1.hashCode();
            result = 31 * result + _2.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Pair{_1=" + _1 + ", _2=" + _2 + '}';
        }
    }


    protected static CList NIL = new Nil();      // neutral value of concatenation

    protected static <T> CList<T> op(CList<T> xs, CList<T> ys) {      // concatenation
        return xs.append(ys);
    }

    public static <T> CList<T> clist(T... elems) {
        return clist(Arrays.asList(elems));
    }

    public static <T> CList<T> clist(List<T> elems) {
        if (elems == null || elems.isEmpty()) {
            return NIL;
        } else {
            T head = elems.get(0);
            List<T> tail = elems.subList(1, elems.size());
            return Cons.<T>cons(head, clist(tail));
        }
    }

    public static <T> Cons cons(T head, CList<T> tail) {
        return new Cons<T>(head, tail);
    }

    public abstract A head();

    public abstract CList<A> tail();

    public abstract Optional<A> headOption();

    public abstract boolean isEmpty();

    public abstract boolean nonEmpty();

    public abstract int length();

    public int size() {
        return length();
    }

    public abstract CList<A> append(CList<A> other);

    public abstract <B> B foldRight(B zero, BiFunction<A, B, B> op);

    public abstract <B> B foldLeft(B zero, BiFunction<B, A, B> op);

    public A fold(A zero, BiFunction<A, A, A> op) {
        return foldRight(zero, op);
    }

    public boolean isDefinedAt(int index) {
        return index >= 0 && index < length();
    }

    public abstract boolean contains(A elem);

    public abstract boolean exists(Predicate<A> p);

    public abstract boolean forall(Predicate<A> p);

    public abstract CList<A> reverse();

    public boolean isPalindrom() {
        return this.equals(this.reverse());
    }

    public abstract CList<A> filter(Predicate<A> p);

    public CList<A> filterNot(Predicate<A> p) {
        return filter(p.negate());
    }

    public abstract <B> CList<B> map(Function<A, B> f);

    public abstract <B> CList<B> flatMap(Function<A, CList<B>> f);

    public abstract CList<A> flatten();

    public CList<A> concat() { return flatten(); }

    public abstract CList<A> take(int n);

    public abstract CList<A> drop(int n);

    public abstract List<A> toList();

    public abstract Set<A> toSet();

    public abstract void foreach(Consumer<A> c);

    public <B, C> CList<C> zipWith(BiFunction<A, B, C> f, CList<B> that) {
        if (this.isEmpty() || that.isEmpty())
            return NIL;
        else
            return cons(f.apply(this.head(), that.head()), tail().zipWith(f, that.tail()));
    }

    public <B> CList<Pair<A, B>> zip(CList<B> that) {
        return zipWith(Pair::new, that);
    }

    public CList<Pair<A, Integer>> zipWithIndex() {
        return CList.zipWithIndex(this, 0);
    }

    private static <T> CList<Pair<T,Integer>> zipWithIndex(CList<T> cl, int index) {
        if (cl.isEmpty())
            return NIL;
        else {
            Pair<T, Integer> head = pair(cl.head(), index);
            CList<Pair<T, Integer>> tail = zipWithIndex(cl.tail(), index + 1);
            return cons(head, tail);
        }
    }

    protected static void println(String message) {
        System.out.println(message);
        System.out.flush();
    }
}

class Nil<A> extends CList<A> {

    @Override
    public A head() {
        throw new NoSuchElementException("head of empty CList");
    }

    @Override
    public CList<A> tail() {
        throw new NoSuchElementException("tail of empty CList");
    }

    @Override
    public Optional<A> headOption() {
        return Optional.empty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean nonEmpty() {
        return false;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public CList<A> append(CList<A> other) {
        return other;
    }

    @Override
    public <B> B foldRight(B zero, BiFunction<A, B, B> op) {
        return zero;
    }

    @Override
    public <B> B foldLeft(B zero, BiFunction<B, A, B> op) {
        return zero;
    }

    @Override
    public boolean contains(A elem) {
        return false;
    }

    @Override
    public boolean exists(Predicate<A> predicate) {
        return false;
    }

    @Override
    public boolean forall(Predicate<A> predicate) {
        return true;
    }

    @Override
    public CList<A> reverse() {
        return this;
    }

    @Override
    public CList<A> filter(Predicate<A> p) {
        return this;
    }

    @Override
    public <B> CList<B> map(Function<A, B> f) {
        return new Nil<B>();
    }

    @Override
    public <B> CList<B> flatMap(Function<A, CList<B>> f) {
        return new Nil<B>();
    }

    @Override
    public CList<A> flatten() {
        return this;
    }

    @Override
    public CList<A> take(int n) {
        return this;
    }

    @Override
    public CList<A> drop(int n) {
        return this;
    }

    @Override
    public List<A> toList() {
        return Collections.emptyList();
    }

    @Override
    public Set<A> toSet() {
        return Collections.emptySet();
    }

    @Override
    public void foreach(Consumer<A> c) {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o != null || getClass() == o.getClass();
    }

    @Override
    public String toString() {
        return "Nil";
    }
}

class Cons<A> extends CList<A> {

    private final A head;
    private final CList<A> tail;

    Cons(A head, CList<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public A head() {
        return head;
    }

    @Override
    public CList<A> tail() {
        return tail;
    }

    @Override
    public Optional<A> headOption() {
        return Optional.of(head);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean nonEmpty() {
        return true;
    }

    @Override
    public int length() {
        return 1 + tail.length();
    }

    @Override
    public CList<A> append(CList<A> other) {
        return cons(head, tail.append(other));
    }

    @Override
    public <B> B foldRight(B zero, BiFunction<A, B, B> op) {
        return op.apply(head, tail.foldRight(zero, op));
    }

    @Override
    public <B> B foldLeft(B zero, BiFunction<B, A, B> op) {
        return op.apply(tail.foldLeft(zero, op), head);
    }

    @Override
    public boolean contains(A elem) {
        return exists(e -> e.equals(elem));
    }

    @Override
    public boolean exists(Predicate<A> p) {
        return foldRight(false, (elem, found) -> found || p.test(elem));
    }

    @Override
    public boolean forall(Predicate<A> p) {
        return !exists(elem -> !p.test(elem));
    }

    private static <T> CList<T> reverse(CList<T> clist, CList<T> acc) {
        return clist.isEmpty() ?
                acc :
                reverse(clist.tail(), cons(clist.head(), acc));
    }

    @Override
    public CList<A> reverse() {
        return reverse(this, new Nil<A>());
    }

    @Override
    public CList<A> filter(Predicate<A> p) {
        return p.test(head) ? cons(head, tail.filter(p)) : tail.filter(p);
    }

    @Override
    public <B> CList<B> map(Function<A, B> f) {
        return cons(f.apply(head), tail.map(f));
    }

    @Override
    public <B> CList<B> flatMap(Function<A, CList<B>> f) {
        return f.apply(head).append(tail.flatMap(f));
    }

    @Override
    public CList<A> flatten() {
        if (head instanceof CList)
            return ((CList<A>) head).append(tail.flatten());
        else
            throw new IllegalStateException("CList cannot be flattened, it is not a CList of CLists.");
    }

    @Override
    public CList<A> take(int n) {
        return n <= 0 ? NIL : cons(head, tail.take(n - 1));
    }

    @Override
    public CList<A> drop(int n) {
        return n <= 0 ? this : tail.drop(n - 1);
    }

    @Override
    public List<A> toList() {
        return foldRight((List<A>) new ArrayList<A>(), Cons::prepend);
    }

    private static <T> List<T> prepend(T x, List<T> xs) {
        xs.add(0, x);
        return xs;
    }

    @Override
    public Set<A> toSet() {
        return foldRight((Set<A>) new HashSet<A>(), Cons::addToSet);
    }

    private static <T> Set<T> addToSet(T elem, Set<T> acc) {
        acc.add(elem);
        return acc;
    }

    @Override
    public void foreach(Consumer<A> c) {
        c.accept(head);
        tail.foreach(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cons<?> cons = (Cons<?>) o;
        if (!head.equals(cons.head)) return false;
        return tail.equals(cons.tail);
    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + tail.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Cons(" + head + ", " + tail + ')';
    }
}
