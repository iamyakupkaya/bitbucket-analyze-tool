// assets
import HomeIcon from '@mui/icons-material/Home';
import ApiIcon from '@mui/icons-material/Api';
import CollectionsBookmarkIcon from '@mui/icons-material/CollectionsBookmark';

// constant
// ==============================|| SAMPLE PAGE & DOCUMENTATION MENU ITEMS ||============================== //

const other = {
    id: 'sample-docs-roadmap',
    type: 'group',
    children: [
        {
            id: 'home-page',
            title: 'Home Page',
            type: 'item',
            url: '/user',
            icon: HomeIcon,
            breadcrumbs: false
        },
        {
            id: 'sample-page',
            title: 'Pull Request',
            type: 'item',
            url: '/user/sample-page',
            icon: ApiIcon,
            breadcrumbs: false
        },
        {
            id: 'sample-page2',
            title: 'Reviewers',
            type: 'item',
            url: '/user/sample-page2',
            icon: CollectionsBookmarkIcon,
            breadcrumbs: false
        }
    ]
};

export default other;
