package org.smartregister.chw.skeleton_sample.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.skeleton.domain.MemberObject;
import org.smartregister.chw.skeleton.fragment.BaseSkeletonCallDialogFragment;

@PrepareForTest(BaseSkeletonCallDialogFragment.class)
public class BaseSkeletonCallDialogFragmentTest {
    @Spy
    public BaseSkeletonCallDialogFragment baseTestCallDialogFragment;

    @Mock
    public ViewGroup viewGroup;

    @Mock
    public View view;

    @Mock
    public MemberObject skeletonMemberObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(BaseSkeletonCallDialogFragment.class, "MEMBER_OBJECT", skeletonMemberObject);
    }

    @Test(expected = Exception.class)
    public void setCallTitleFamilyHead() throws Exception {
        TextView textView = Mockito.mock(TextView.class);

        Mockito.when(skeletonMemberObject.getBaseEntityId()).thenReturn("123456");
        Mockito.when(skeletonMemberObject.getFamilyHead()).thenReturn("123456");

        Mockito.when(viewGroup.findViewById(view.getId())).thenReturn(textView);

        Whitebox.invokeMethod(baseTestCallDialogFragment, "setCallTitle", viewGroup, view.getId(), "message");
        Assert.assertEquals("message Head of family", textView.getText());
    }

    @Test(expected = Exception.class)
    public void setCallTitleAnc() throws Exception {
        TextView textView = Mockito.mock(TextView.class);

        Mockito.when(viewGroup.findViewById(view.getId())).thenReturn(textView);

        Whitebox.invokeMethod(baseTestCallDialogFragment, "setCallTitle", viewGroup, view.getId(), "message");
        Assert.assertEquals("message ANC Client", textView.getText());
    }

    @Test(expected = Exception.class)
    public void setCallTitleCareGiver() throws Exception {
        TextView textView = Mockito.mock(TextView.class);

        Mockito.when(skeletonMemberObject.getBaseEntityId()).thenReturn("123456");
        Mockito.when(skeletonMemberObject.getPrimaryCareGiver()).thenReturn("123456");

        Mockito.when(viewGroup.findViewById(view.getId())).thenReturn(textView);

        Whitebox.invokeMethod(baseTestCallDialogFragment, "setCallTitle", viewGroup, view.getId(), "message");
        Assert.assertEquals("message Primary Caregiver", textView.getText());
    }

    @Test(expected = Exception.class)
    public void setCallTitlePnc() throws Exception {
        TextView textView = Mockito.mock(TextView.class);

        Mockito.when(viewGroup.findViewById(view.getId())).thenReturn(textView);

        Whitebox.invokeMethod(baseTestCallDialogFragment, "setCallTitle", viewGroup, view.getId(), "message");
        Assert.assertEquals("message PNC Client", textView.getText());
    }

    @Test(expected = Exception.class)
    public void setCallTitle() throws Exception {
        TextView textView = Mockito.mock(TextView.class);

        Mockito.when(skeletonMemberObject.getBaseEntityId()).thenReturn("1");
        Mockito.when(skeletonMemberObject.getFamilyHead()).thenReturn("123456");

        Mockito.when(viewGroup.findViewById(view.getId())).thenReturn(textView);

        Whitebox.invokeMethod(baseTestCallDialogFragment, "setCallTitle", viewGroup, view.getId(), "message");
        Assert.assertEquals("message Skeleton Client", textView.getText());
    }

    @Test(expected = Exception.class)
    public void initUI() throws Exception {
        Mockito.when(skeletonMemberObject.getPhoneNumber()).thenReturn("123456789");
        Whitebox.invokeMethod(baseTestCallDialogFragment, "initUI", viewGroup);
        PowerMockito.verifyPrivate(baseTestCallDialogFragment).invoke("setCallTitle", viewGroup, view.getId(), "message");

    }
}
