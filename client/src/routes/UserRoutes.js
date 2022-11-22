import { lazy } from 'react';

// project imports
import UserLayout from 'layout/UserLayout';
import Loadable from 'ui-component/Loadable';

// sample page routing
const PullRequestPage = Loadable(lazy(() => import('views/user/pull-request')));
const SamplePage2 = Loadable(lazy(() => import('views/user/sample-page2')));
const HomePage = Loadable(lazy(() => import('views/user/home-page')));


// ==============================|| MAIN ROUTING ||============================== //

const MainRoutes = {
    path: '/user/',
    element: <UserLayout />,
    children: [
        {
            path: '',
            element: <HomePage />
        },
        {
            path: 'sample-page',
            element: <PullRequestPage />
        },
        {
            path: 'sample-page2',
            element: <SamplePage2 />
        }
    ]
};

export default MainRoutes;
