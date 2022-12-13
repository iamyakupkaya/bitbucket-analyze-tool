// assets
import HomeIcon from '@mui/icons-material/Home';
import ApiIcon from '@mui/icons-material/Api';
import ContactsIcon from '@mui/icons-material/Contacts';
import Diversity2Icon from '@mui/icons-material/Diversity2';
// constant
// ==============================|| SAMPLE PAGE & DOCUMENTATION MENU ITEMS ||============================== //

const other = {
    id: 'sample-docs-roadmap',
    type: 'group',
    children: [
        {
            id: 'home',
            title: 'Home Page',
            type: 'item',
            url: '/user/home',
            icon: HomeIcon,
            breadcrumbs: false
        },
        {
            id: 'pull-requests',
            title: 'Pull Request',
            type: 'item',
            url: '/user/pull-requests',
            icon: ApiIcon,
            breadcrumbs: false
        },
        {
            id: 'authors',
            title: 'Authors',
            type: 'item',
            url: '/user/authors',
            icon: ContactsIcon,
            breadcrumbs: false
        }

    ]
};

export default other;
