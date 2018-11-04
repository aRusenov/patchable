import models.SimpleBean;
import bg.arusenov.patchable.Patchable;
import org.junit.Assert;
import org.junit.Test;

public class JavaTest {

    @Test
    public void notSetPatchable_ShouldNotCall_IfSetConsumer() {
        SimpleBean bean = new SimpleBean("A", 32);
        Patchable<String> name = Patchable.of("B");
        Patchable<Integer> age = Patchable.notSet();

        name.ifSet(bean::setName);
        age.ifSet(bean::setAge);

        Assert.assertEquals("B", bean.getName());
        Assert.assertEquals(32, bean.getAge());
    }
}
