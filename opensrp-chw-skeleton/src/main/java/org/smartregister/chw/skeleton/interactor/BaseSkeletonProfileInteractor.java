package org.smartregister.chw.skeleton.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.skeleton.contract.SkeletonProfileContract;
import org.smartregister.chw.skeleton.domain.MemberObject;
import org.smartregister.chw.skeleton.util.AppExecutors;
import org.smartregister.chw.skeleton.util.SkeletonUtil;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public class BaseSkeletonProfileInteractor implements SkeletonProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseSkeletonProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseSkeletonProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo(MemberObject memberObject, SkeletonProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callback.refreshFamilyStatus(AlertStatus.normal);
            callback.refreshMedicalHistory(true);
            callback.refreshUpComingServicesStatus("Skeleton Visit", AlertStatus.normal, new Date());
        });
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(final String jsonString, final SkeletonProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                SkeletonUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }
}
