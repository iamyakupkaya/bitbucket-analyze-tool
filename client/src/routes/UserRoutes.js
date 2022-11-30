import { lazy } from 'react';

// project imports
import UserLayout from 'layout/UserLayout';
import Loadable from 'ui-component/Loadable';

// sample page routing
const PullRequestPage = Loadable(lazy(() => import('views/user/pull-request')));
const AuthorPage = Loadable(lazy(() => import('views/user/author-page')));
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
            element: <AuthorPage />
        }
    ]
};

export default MainRoutes;
